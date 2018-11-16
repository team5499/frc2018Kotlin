package frc.team5499.frc2018Kotlin.dashboard.DashboardClient

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

object DashboardClient {
    private val opts: IO.Options = IO.Options()

    init {
        opts.forceNew = true
        opts.reconnection = true
    }

    private val socket: Socket = IO.socket("http://localhost:5800", opts)

    init {
        socket.on(Socket.EVENT_CONNECT, Emitter.Listener() {
            fun call(vararg args: Any) {
                socket.emit("foo", "hi")
                socket.disconnect()
            }
        }).on(Socket.EVENT_MESSAGE, Emitter.Listener() {
            fun call(vararg args: Any) {
            }
        })
    }
}
