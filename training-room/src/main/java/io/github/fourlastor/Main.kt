package io.github.fourlastor

import io.github.fourlastor.rpc.TrainingServer

fun main() {
    val port = 8980
    val server = TrainingServer(port)
    server.start()
    server.blockUntilShutdown()
}
