import grpc

from trainer.src.generated.game_pb2 import Action, Actions, GameInfo, ActionType
from trainer.src.generated.game_pb2_grpc import GameStub
from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2


class RpcClient:
    def __init__(self, address: str, port: int):
        self.target = f"{address}:{port}"

    def update(self, action_1: ActionType, action_2: ActionType) -> GameInfo:
        with grpc.insecure_channel(self.target) as channel:
            stub = GameStub(channel)
            return stub.Update(
                Actions(
                    p1=Action(type=action_1),
                    p2=Action(type=action_2),
                )
            )

    def reset(self) -> GameInfo:
        with grpc.insecure_channel(self.target) as channel:
            stub = GameStub(channel)
            return stub.Reset(google_dot_protobuf_dot_empty__pb2.Empty())
