package org.Controller

import Canvas.Interfaces.KeyHookInterface
import Main.KeyHook
import Main.WindowTest
import javafx.application.Platform
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.UIManager

class Binder: TrayInterface, KeyHookInterface, UIInterface {

    companion object {
        var uiInterface: UIInterface? = null
    }

    val tray: Tray
    val keyHook: KeyHook
    var visibility = false

    init {
        System.setProperty("apple.awt.UIElement", "true")
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: Exception) {
            System.err.println(e.localizedMessage)
        }

        uiInterface = this
        tray = Tray(this)
        keyHook = KeyHook({ Platform.runLater { toggle() } }, { Platform.runLater { /* key combo released */ } })
        WindowTest.launch()
    }

    fun toggle() {
        Platform.runLater {
            if (visibility) {
                WindowTest.instance.hide()
            } else {
                WindowTest.instance.show()
            }
            visibility = !visibility
        }
    }

    //KeyHookInterface
    override fun shouldIntercept(): Boolean {
        return visibility
    }

    // TrayInterface
    override fun togglePanel() = toggle()
    override fun exit() = System.exit(0)

    // UIInterface
    override fun toggleBar() = toggle()
}

interface UIInterface {
    fun toggleBar()
}

class Tray(var trayInterface: TrayInterface) {

    var trayIcon: TrayIcon? = null

    init {
        if (SystemTray.isSupported()) {
            val image = ImageIO.read(javaClass.classLoader.getResource("pen.png"))
            trayIcon = TrayIcon(image, "Dropit",
                    PopupMenu()
                            .apply { add(MenuItem("Toggle panel").apply {
                                addActionListener { trayInterface.togglePanel() }}) }
                            .apply { add(MenuItem("Exit").apply {
                                addActionListener { trayInterface.exit() }}) }
            ).apply { addActionListener { trayInterface.togglePanel() }}

            try {
                SystemTray.getSystemTray().add(trayIcon)
            } catch (e: AWTException) {
                System.err.println(e)
            }
        } else {
            println("TRAY NOT SUPPORTED")
        }
    }

    fun changeImage(image: BufferedImage) {
        trayIcon?.setImage(image)
    }
}

interface TrayInterface {
    fun togglePanel()
    fun exit()
}

fun String.p() {
    println(this)
}