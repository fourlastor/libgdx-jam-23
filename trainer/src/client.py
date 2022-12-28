import grpc

from trainer.src.generated.game_pb2 import Action, Attack, Left, Actions
from trainer.src.generated.game_pb2_grpc import GameStub


def run():
    with grpc.insecure_channel('localhost:8980') as channel:
        stub = GameStub(channel)
        state = stub.Update(Actions(
            p1 = Action(type=Attack),
            p2 = Action(type=Left),
        ))
        print(state)


if __name__ == "__main__":
    run()
