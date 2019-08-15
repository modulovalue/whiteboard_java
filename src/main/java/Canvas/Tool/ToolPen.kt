package Canvas.Tool

import Canvas.aPrimitives.DrawbleObj
import Canvas.aPrimitives.Line3
import Canvas.aPrimitives.ObjectFactory
import Canvas.aPrimitives.PointDragDraw
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class ToolPen(opacity: Double = 1.0, strokeWidth: Double = 5.0) : ClickAndDragTool() {

    internal var opacity = 1.0
    internal var strokeWidth = 5.0

    internal var curObject: PointDragDraw? = null

    init {
        this.opacity = opacity
        this.strokeWidth = strokeWidth
    }

    override fun mousePressedHandler(event: MouseEvent) {
        if (event.isPrimaryButtonDown) {
            val mousePoint = getEventPoint(event)
            start(mousePoint.x, mousePoint.y)
        }
    }

    override fun mouseDraggedHandler(event: MouseEvent) {
        if (event.isPrimaryButtonDown) {
            if (curObject == null) return
            val mousePoint = getEventPoint(event)
            end(mousePoint.x, mousePoint.y)
            start(mousePoint.x, mousePoint.y)
        }
    }

    override fun mouseReleasedHandler(event: MouseEvent) {
        if (curObject != null) {
            val mousePoint = getEventPoint(event)
            end(mousePoint.x, mousePoint.y)
        }
    }

    fun start(x: Double, y: Double) {
        curObject = ObjectFactory.getObject("Line", x, y, Color.RED, strokeWidth, Color.RED.deriveColor(1.0, 1.0, 1.0, opacity))
        canvasInterface.addNewObject(curObject as DrawbleObj)
    }

    fun end(x: Double, y: Double) {
        if (x > 0 && x > 0) {
            (curObject as Line3).node.endX = x
            (curObject as Line3).node.endY = y
            curObject = null
        }
    }
}


