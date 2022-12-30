from typing import Tuple

import gymnasium
import numpy as np

import gymnasium as gym
from gymnasium import spaces, register
from gymnasium.core import RenderFrame

from trainer.src.client import RpcClient
from trainer.src.generated.game_pb2 import (
    ActionType,
    Idle,
    Left,
    Right,
    Attack,
    GameInfo,
)

FIGHTER_COUNT = 3
MAX_X = 26
MAX_Y = 6
MAX_HEALTH = 1
ACTION_COUNT = 4


class KampenisseEnv(gym.Env[dict, Tuple[int]]):
    def render(self) -> RenderFrame | list[RenderFrame] | None:
        pass

    def __init__(self, address: str = "localhost", port: int = 8980):
        self.rpc_client = RpcClient(address, port)

        # Observation space:
        # (fighter_1, x_1, y_1, health_1, current_action_1, fighter_2, x_2, y_2, health_2, current_action_2)
        position_low = np.array([0, 0], dtype=np.float32)
        position_high = np.array([MAX_X, MAX_Y], dtype=np.float32)
        health_low = np.array([0], dtype=np.float32)
        health_high = np.array([MAX_HEALTH], dtype=np.float32)

        p_space = spaces.Dict(
            {
                "fighter": spaces.Discrete(FIGHTER_COUNT),
                "position": spaces.Box(low=position_low, high=position_high),
                "health": spaces.Box(low=health_low, high=health_high, dtype=np.float32),
                "current_action": spaces.Discrete(ACTION_COUNT),
            }
        )
        self.observation_space = spaces.Dict({"p1": p_space, "p2": p_space})

        self.action_space = spaces.Discrete(ACTION_COUNT)

        self.render_mode = None

    @staticmethod
    def _convert_action(action: int) -> ActionType:
        if action == 0:
            return Idle
        elif action == 1:
            return Left
        elif action == 2:
            return Right
        else:
            return Attack

    @staticmethod
    def _convert_observation(observation: GameInfo) -> dict:
        p1 = observation.p1
        p2 = observation.p2

        return {
            "p1": {
                "fighter": p1.fighter,
                "position": np.array([p1.position.x, p1.position.y], dtype=np.float32),
                "health": np.array([p1.health], dtype=np.float32),
                "current_action": p1.currentAction,
            },
            "p2": {
                "fighter": p2.fighter,
                "position": np.array([p2.position.x, p2.position.y], dtype=np.float32),
                "health": np.array([p2.health], dtype=np.float32),
                "current_action": p2.currentAction,
            },
        }

    def reset(
        self,
        seed=None,
        options=None,
    ) -> tuple[dict, dict]:

        # return self.observation_space.sample(), {}
        return self._convert_observation(self.rpc_client.reset()), {}

    def step(self, actions: Tuple[int]) -> Tuple[dict, Tuple[float, float], bool, bool, dict]:
        observation = self.rpc_client.update(self._convert_action(actions[0]), self._convert_action(actions[1]))

        # TODO reward computation
        reward = (100, 100)

        # terminated if health of one of the two fighters is 0
        terminated = observation.p1.health == 0 or observation.p2.health == 0

        return self._convert_observation(observation), reward, terminated, False, {}


if __name__ == "__main__":
    register(
        id="kampenisse-v0",
        entry_point="trainer.src.envs.kampenisse:KampenisseEnv",
        max_episode_steps=300,
    )
    env = gymnasium.make("kampenisse-v0")
    print(env.reset())
    print(env.step(action=[0, 1]))
