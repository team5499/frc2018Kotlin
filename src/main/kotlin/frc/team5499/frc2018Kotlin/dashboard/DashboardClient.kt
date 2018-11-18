package frc.team5499.frc2018Kotlin.dashboard

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.FilterBuilder

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

        var classLoadersList = mutableListOf<ClassLoader>()
        classLoadersList.add(ClasspathHelper.contextClassLoader())
        classLoadersList.add(ClasspathHelper.staticClassLoader())

        var reflections: Reflections = Reflections(ConfigurationBuilder()
            .setScanners(SubTypesScanner(false /* don't exclude Object.class */), ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList[0]))
            .filterInputsBy(FilterBuilder().include(FilterBuilder.prefix("tests"))))

        var classes: Set<Class<*>> = reflections.getSubTypesOf(Any::class.java)

        println(classes)
    }

    public fun testFun() {
        println("test")
    }
}
