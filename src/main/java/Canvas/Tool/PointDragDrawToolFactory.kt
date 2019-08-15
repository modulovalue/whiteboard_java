package Canvas.Tool

import Canvas.aPrimitives.DrawbleObj
import Canvas.aPrimitives.ObjectFactory
import Canvas.aPrimitives.PointDragDraw
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

class PointDragDrawToolFactory(
        internal var type: String,
        var strokeColor: Color,
        var innerColor: Color,
        var strokeWidth: Double) : ClickAndDragTool() {

    internal var curObject: PointDragDraw? = null

    override fun mousePressedHandler(event: MouseEvent) {
        if (event.isPrimaryButtonDown) {
            curObject = ObjectFactory.getObject(type, event.x, event.y, Paint.valueOf(innerColor.toString()), strokeWidth , Paint.valueOf(strokeColor.toString()))
            val p = getEventPoint(event)
            curObject!!.setStart(p.x, p.y)
            canvasInterface.addNewObject(curObject as DrawbleObj?)
        }
    }

    override fun mouseDraggedHandler(event: MouseEvent) {
        if (event.isPrimaryButtonDown) {
            if (curObject != null) {
                if (event.x > 0 && event.y > 0) {
                    val p = getEventPoint(event)
                    curObject!!.setEnd(p.x, p.y)
                }
            }
        }
    }

    override fun mouseReleasedHandler(event: MouseEvent) {
        curObject = null
    }
}
