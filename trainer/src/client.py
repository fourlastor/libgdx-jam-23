import grpc


def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = GameStub(channel)
        state = stub.Update(Actions(
            p1 = ,
            p2 = ,
        ))
