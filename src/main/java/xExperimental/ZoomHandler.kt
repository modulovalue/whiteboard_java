package xExperimental

import Canvas.Canvas.CanvasLowLevel.Canvas2
import javafx.geometry.Point2D
import javafx.scene.control.ScrollPane
import javafx.scene.input.ZoomEvent
import javafx.scene.layout.Pane
import javafx.scene.transform.Scale

public class ZoomHandler {

    var zoomPoint: Point2D? = null
    var startedZooming = false

//    fun initZooming(interactionNode: Node, scrollPane: ScrollPane, getDrawingPane: Pane, canvas: Canvas2.Canvas2.CanvasLowLevel.Canvas2) {
        //interactionNode.setOnZoomStarted {
        //    zoomPoint = getDrawingPane.screenToLocal(it.x, it.y)
        //}
//        interactionNode.setOnZoom {
//            zoomHandler(it, getDrawingPane, scrollPane, canvas)
//        }
        //interactionNode.setOnZoomFinished {
        //    //startedZooming = false
        //}
//    }

//    fun startTimeline(scrollPane: ScrollPane, getDrawingPane: Pane) {
//        val timeline = Timeline()
//        val kv = KeyValue(scrollPane.hvalueProperty(), zoomPoint!!.x / getDrawingPane.minWidth, Interpolator.EASE_BOTH)
//        val kv2 = KeyValue(scrollPane.vvalueProperty(), zoomPoint!!.y / getDrawingPane.minHeight, Interpolator.EASE_BOTH)
//        val kf = KeyFrame(Duration.millis(500.0), kv, kv2)
//        timeline.keyFrames.add(kf)
//        timeline.play()
//        timeline.setOnFinished { event ->
//            if (startedZooming) {
//                timeline.playFromStart()
//            }
//        }
//    }

    fun zoomHandler(e: ZoomEvent, drawingPane: Pane, scrollPane: ScrollPane, canvas: Canvas2) {
        e.consume()
        val zoomType = 1
        if (zoomType == 1) {
            val minScale = 0.5
            val maxScale = 10.0
            val oldScale = drawingPane.scaleX
            var scale = oldScale * e.zoomFactor
            if (scale < minScale) scale = minScale
            if (scale > maxScale) scale = maxScale

            // TODO divided by viewport bounds, try that again
//            var zp2 = getDrawingPane.screenToLocal(e.x, e.y)
//            var dx = zoomPoint!!.x - zp2.x
//            var dy = zoomPoint!!.y - zp2.y
//            var ds = oldScale - scale
//
//            var width = scrollPane.getViewportBounds().width
//            var height = scrollPane.getViewportBounds().height

//            scrollPane.hvalue = scrollPane.hvalue + (dx / width)
//            scrollPane.vvalue = scrollPane.vvalue + (dy / height)
            canvas.canvasPane.scaleXProperty().set(scale)
            canvas.canvasPane.scaleYProperty().set(scale)

//            println(" " + zoomPoint!!.x + " " + zp2.x + " " + ds + " " + (dx / width))
//            zoomPoint = zp2

        } else if (zoomType == 2) {
            // LA
            val newScale = Scale()
            newScale.pivotX = e.x
            newScale.pivotY = e.y
            newScale.x = e.zoomFactor
            newScale.y = e.zoomFactor
            drawingPane.transforms.add(newScale)
            e.consume()
        } else {
            drawingPane.scaleX = drawingPane.scaleX * e.zoomFactor
            drawingPane.scaleY = drawingPane.scaleY * e.zoomFactor
        }

    }
}