package Canvas.Canvas

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.input.ZoomEvent.*
import javafx.scene.layout.Pane


class ZoomHelper {
    companion object {
        fun zoom(pane: Pane, scrollPane: ScrollPane, eventHandler: Node, viewHeights: () -> Pair<Double, Double>){
            var point: Point2D = Point2D.ZERO
            var oldhvalue: Double? = null
            var oldvvalue: Double? = null

            eventHandler.addEventHandler(ZOOM_STARTED, {
                point = pane.sceneToLocal(Point2D(it.sceneX, it.sceneY))
                oldhvalue = scrollPane.hvalue
                oldvvalue = scrollPane.vvalue
            })

            eventHandler.addEventHandler(ZOOM_FINISHED, {
                oldhvalue = null
                oldvvalue = null
            })

            var viewHeight = viewHeights.invoke();
            var minZoom = viewHeight.first / viewHeight.second

            eventHandler.addEventHandler(ZOOM, {
                zoom(pane, scrollPane, it.zoomFactor)

//                val contentHeight = scrollPane.boundsInParent.height
//                val contentWidth = scrollPane.boundsInParent.width
//
//                var scaleDif1 = (newScale / pane.scaleX) - 1


//                scrollPane.hvalue = oldhvalue!! + (point.x - 0.5 * scrollPane.viewportBounds.width) / (contentWidth - scrollPane.viewportBounds.width)
//                scrollPane.vvalue = oldhvalue!! + (point.y - 0.5 * scrollPane.viewportBounds.height) / (contentHeight - scrollPane.viewportBounds.height)


//                var newHValue = (point.x - 0.5 * scrollPane.viewportBounds.width) / (contentWidth - scrollPane.viewportBounds.width)
//                var newVValue = (point.y - 0.5 * scrollPane.viewportBounds.height) / (contentHeight - scrollPane.viewportBounds.height)

//                var newHValue = oldhvalue!! + (point.x - 0.5 * scrollPane.viewportBounds.width) / (contentWidth - scrollPane.viewportBounds.width)
//                var newVValue = oldhvalue!! + (point.y - 0.5 * scrollPane.viewportBounds.height) / (contentHeight - scrollPane.viewportBounds.height)
//
//                println(it.totalZoomFactor)
//
//                scrollPane.hvalue = (newHValue * (it.totalZoomFactor - 1))
//                scrollPane.vvalue = (newVValue * (it.totalZoomFactor - 1))


//                scrollPane.hvalue = oldhvalue!! - (point.x / contentWidth ) * scaleDif1
//                scrollPane.vvalue = oldvvalue!! - (point.y / contentHeight ) * scaleDif1
//

//                scrollPane.setVvalue(scrollPane.getVvalue() + point.x / (pane.getBoundsInParent().getHeight() / 2 + pane.getBoundsInParent().getMinY()))
//                scrollPane.setHvalue(scrollPane.getHvalue() + point.y / (pane.getBoundsInParent().getWidth() / 2 + pane.getBoundsInParent().getMinX()))

                scrollPane.hvalue = oldhvalue!!
                scrollPane.vvalue = oldvvalue!!

//                pane.getChildren().addAll(Circle(point.x, point.y, 11.0, Color.GRAY))
            })
        }

        fun zoom(pane: Pane, scrollPane: ScrollPane, factor: Double) {
            var widthZoom = scrollPane.layoutBounds.height / pane.minHeight
            var heightZoom = scrollPane.layoutBounds.width / pane.minWidth
            var newScale = Math.max(Math.max(widthZoom, heightZoom), Math.min(10.0, pane.scaleX * factor))
            pane.scaleXProperty().set(newScale)
            pane.scaleYProperty().set(newScale)
        }
    }
}
