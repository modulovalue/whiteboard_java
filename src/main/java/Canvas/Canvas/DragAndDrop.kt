package Canvas.Canvas

import Canvas.Interfaces.DragAndDropInterface
import javafx.scene.Scene
import javafx.scene.input.TransferMode
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.net.MalformedURLException
import java.net.URL

internal class DragAndDrop(
        scene: Scene,
        var dragAndDropInterface: DragAndDropInterface) {

    init {
        setupDragAndDrop(scene)
    }

    private fun setupDragAndDrop(scene: Scene) {
        scene.setOnDragOver { event ->
            val db = event.dragboard
            if (db.hasFiles()) {
                if (isImage(db.files[0])) {
                    event.acceptTransferModes(TransferMode.COPY)
                } else if (db.files[0].isDirectory) {
                    event.acceptTransferModes(TransferMode.COPY)
                }
            } else if (db.hasUrl() || db.hasString()) {
                event.acceptTransferModes(TransferMode.COPY)
            } else {
                event.acceptTransferModes(*TransferMode.NONE)
                event.consume()
            }
        }

        scene.setOnDragDropped { e ->
            val db = e.dragboard
            if (db.hasFiles()) {
                if (isImage(db.files[0])) {
                    dragAndDropInterface.newImage(db.files[0], e)
                } else if (db.files[0].isDirectory) {
                    dragAndDropInterface.newDirectory(db.files[0], e)
                }
            } else if (db.hasUrl()) {
                dragAndDropInterface.newLink(db.url, e)
            } else if (db.hasString()) {
                if (isURL(db.string)) {
                    dragAndDropInterface.newLink(db.string, e)
                } else {
                    // only text TODO
                }
            } else {
                e.isDropCompleted = false
                e.consume()
            }
            e.isDropCompleted = true
            e.consume()
        }
    }

    private fun isURL(string: String): Boolean {
        try {
            val url = URL(string)
            return true
        } catch (e: MalformedURLException) {
            return false
        }

    }

    private fun isImage(str: File): Boolean {
        val allowedExtensions = arrayOf("jpeg", "jpg", "png")
        return FilenameUtils.isExtension(str.toPath().toString().toLowerCase(), allowedExtensions)
    }
}