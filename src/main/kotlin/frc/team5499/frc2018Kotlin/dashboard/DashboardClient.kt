package frc.team5499.frc2018Kotlin.dashboard

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

object DashboardClient {
    private val opts = IO.Options().apply {
        forceNew = true
        reconnection = true
    }

    private val socket: Socket = IO.socket("http://localhost:5800", opts)

    init {
        println("socket shit")
        socket.on(Socket.EVENT_CONNECT, Emitter.Listener() {
            fun call(vararg args: Any) {
                println("connection")
            }
        }).on(Socket.EVENT_MESSAGE, Emitter.Listener() {
            fun call(vararg args: Any) {
                println("message")
            }
        })
        socket.connect()
    }

    public fun testFun() {
        println("test")
    }
}
