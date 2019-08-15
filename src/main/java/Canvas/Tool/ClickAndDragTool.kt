package Canvas.Tool

import Canvas.Interfaces.CanvasInterface
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

abstract class ClickAndDragTool : DrawableTool {

    internal lateinit var canvasInterface: CanvasInterface

    override fun setToolActive(canvasInterface: CanvasInterface) {
        this.canvasInterface = canvasInterface
        this.canvasInterface.drawingPane.onMousePressed = EventHandler<MouseEvent> { this.mousePressedHandler(it) }
        this.canvasInterface.drawingPane.onMouseDragged = EventHandler<MouseEvent> { this.mouseDraggedHandler(it) }
        this.canvasInterface.drawingPane.onMouseReleased = EventHandler<MouseEvent> { this.mouseReleasedHandler(it) }
    }

    override fun setToolInactive() {
        canvasInterface.drawingPane.setOnMousePressed { event -> }
        canvasInterface.drawingPane.setOnMouseDragged { event -> }
        canvasInterface.drawingPane.setOnMouseReleased { event -> }
    }

    fun getEventPoint(event: MouseEvent): Point2D {
        var x = 0.0
        var y = 0.0
        if (event.x > canvasInterface.drawingPane.minWidth) {
            x = canvasInterface.drawingPane.minWidth
        } else if (event.x < 0) {
            x = 0.0
        } else {
            x = event.x
        }
        if (event.y > canvasInterface.drawingPane.minHeight) {
            y = canvasInterface.drawingPane.minHeight
        } else if (event.y < 0) {
            y = 0.0
        } else {
            y = event.y
        }
        return Point2D(x, y)
    }

    abstract fun mousePressedHandler(event: MouseEvent)

    abstract fun mouseDraggedHandler(event: MouseEvent)

    abstract fun mouseReleasedHandler(event: MouseEvent)
}

interface DrawableTool {
    fun setToolActive(canvasInterface: CanvasInterface)
    fun setToolInactive()
}