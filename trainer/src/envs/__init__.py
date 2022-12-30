from gymnasium import register

register(
    id="kampenisse-v0",
    entry_point="trainer.src.envs.kampenisse:KampenisseEnv",
    max_episode_steps=100_000,
)
