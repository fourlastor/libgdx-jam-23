package io.github.fourlastor.rpc

class GameService : GameGrpcKt.GameCoroutineImplBase() {
    override suspend fun update(request: Actions): GameInfo {
        TODO()
    }
}
