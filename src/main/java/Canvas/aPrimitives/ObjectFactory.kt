package Canvas.aPrimitives

import Canvas.Interfaces.SelectableInterface
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.RotateEvent
import javafx.scene.input.ZoomEvent
import javafx.scene.paint.Paint

class ObjectFactory {
    companion object {
        fun getImg(name: String, currentPos: Point2D, data: String, type: String, iai: ImageActionInterface, url: String? = null): DrawbleObj {
            when (type) {
                "Directory" -> return Image3(name, "/Directory.png", 1.0, 0.0, currentPos.x, currentPos.y, data, type, iai)
                "Link" -> return Image3(name, "/Link.png", 1.0, 0.0, currentPos.x, currentPos.y, data, type, iai)
                "Image" -> return Image3(name, url!!, 1.0, 0.0, currentPos.x, currentPos.y, data, type, iai)
                else -> throw ClassNotFoundException("Primitive does not exist")
            }
        }
        fun getObject(type: String, startX: Double, startY: Double, color: Paint, strokeWidth: Double, strokeColor: Paint): PointDragDraw {
            when (type) {
                "Circle" -> return Circle3(startX, startY, 0.0, color.toString(), strokeColor.toString(), strokeWidth)
                "Line" -> return Line3(startX, startY, startX, startY, color.toString(), strokeColor.toString(), strokeWidth)
                "Rectangle" -> return Rectangle3(startX, startY, 0.0, 0.0, color.toString(), strokeColor.toString(), strokeWidth)
                else -> throw ClassNotFoundException("Primitive does not exist")
            }
        }
    }
}

class NodeGestures(internal var drawableObjectImage: DrawbleObj, internal var selectableInterface: SelectableInterface) {

    internal var mouseAnchorX: Double = 0.toDouble()
    internal var mouseAnchorY: Double = 0.toDouble()
    internal var translateAnchorX: Double = 0.toDouble()
    internal var translateAnchorY: Double = 0.toDouble()

    companion object {
        var MAX_ZOOM = 20.0
        var MIN_ZOOM = 0.1
    }

    init {
        drawableObjectImage.node.apply {
            addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler)
            addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler)
            setOnMouseClicked({ mouseEvent ->
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (mouseEvent.getClickCount() == 2) {
                        drawableObjectImage.editMode = !drawableObjectImage.editMode
                        if (drawableObjectImage.editMode) {
                            addEventFilter(ZoomEvent.ZOOM, zoomEventEventHandler)
                            addEventFilter(RotateEvent.ROTATE, rotateEventEventHandler)
                        } else {
                            removeEventFilter(ZoomEvent.ZOOM, zoomEventEventHandler)
                            removeEventFilter(RotateEvent.ROTATE, rotateEventEventHandler)
                        }
                        selectableInterface.resizeMode(drawableObjectImage.editMode)
                        mouseEvent.consume()
                    }
                }
            })
        }
    }

    val onMousePressedEventHandler: EventHandler<MouseEvent>
        get() = EventHandler { event ->
            if (event.isPrimaryButtonDown()) {
                mouseAnchorX = event.getSceneX()
                mouseAnchorY = event.getSceneY()
                val node = event.getSource() as Node
                translateAnchorX = node.translateX
                translateAnchorY = node.translateY
                selectableInterface.selected(drawableObjectImage)
                event.consume()
            }
        }

    val onMouseDraggedEventHandler: EventHandler<MouseEvent>
        get() = EventHandler { event ->
            if (event.isPrimaryButtonDown()) {
                val scale = selectableInterface.getDrawingPane().scaleX
                val node = event.getSource() as Node
                node.translateX = translateAnchorX + (event.getSceneX() - mouseAnchorX) / scale
                node.translateY = translateAnchorY + (event.getSceneY() - mouseAnchorY) / scale
                event.consume()
            }
        }

    val zoomEventEventHandler: EventHandler<ZoomEvent>
        get() = EventHandler { event ->
            drawableObjectImage.scaleBy(event.zoomFactor, MIN_ZOOM, MAX_ZOOM)
            event.consume()
        }

    val rotateEventEventHandler: EventHandler<RotateEvent>
        get() = EventHandler { event ->
            drawableObjectImage.rotateBy(event.angle)
            event.consume()
        }
}
