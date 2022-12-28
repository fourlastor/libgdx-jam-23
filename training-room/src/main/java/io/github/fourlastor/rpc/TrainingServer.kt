package io.github.fourlastor.rpc

import io.github.fourlastor.game.GameComponent
import io.grpc.Server
import io.grpc.ServerBuilder

class TrainingServer(port: Int) {
    private val server: Server

    init {
        val service = GameComponent.component().service()
        server = ServerBuilder.forPort(port).addService(service).build()
    }

    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
        })
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}
