package Main

import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.*
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class KeyHook(var toCall: () -> Unit, var toCloseCall: () -> Unit): NativeKeyListener {

    var flags = EnumSet.noneOf(KeyHookKeys::class.java)
    var called = false

    init {
        try {
            GlobalScreen.setEventDispatcher(VoidDispatchService())
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            System.err.println(ex.message)

            System.exit(1)
        }

        val logger = Logger.getLogger(GlobalScreen::class.java.`package`.name)
        logger.setLevel(Level.OFF)

        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
      //  println("Key Pressed: " + NativeKeyEvent.getKeyText(e.keyCode) + " keycode " + e.keyCode)
        when (e.keyCode) {
            // COMMAND Key
            KeyHookKeys.COMMAND.value -> {
                flags.add(KeyHookKeys.COMMAND)
            }
            // < > Key
            KeyHookKeys.LESSBIGGER.value -> {
                flags.add(KeyHookKeys.LESSBIGGER)
            }
        }

        if (flags.contains(KeyHookKeys.COMMAND) && flags.contains(KeyHookKeys.LESSBIGGER) && !called) {
            toCall()
            called = true
        }
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        when (e.keyCode) {
            // COMMAND Key
            KeyHookKeys.COMMAND.value -> {
                flags.remove(KeyHookKeys.COMMAND)
            }
            // > < Key
            KeyHookKeys.LESSBIGGER.value -> {
                flags.remove(KeyHookKeys.LESSBIGGER)
            }
        }
        toCloseCall()
        called = false
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {

    }

}

enum class KeyHookKeys(val value: Int) {
    COMMAND(3675), LESSBIGGER(41)
}


class VoidDispatchService : AbstractExecutorService() {

    private var running = false

    init {
        running = true
    }

    override fun shutdown() {
        running = false
    }

    override fun shutdownNow(): List<Runnable> {
        running = false
        return ArrayList(0)
    }

    override fun isShutdown(): Boolean {
        return !running
    }

    override fun isTerminated(): Boolean {
        return !running
    }

    @Throws(InterruptedException::class)
    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
        return true
    }

    override fun execute(r: Runnable) {
        r.run()
    }
}