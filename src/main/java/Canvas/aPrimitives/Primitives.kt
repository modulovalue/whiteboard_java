package Canvas.aPrimitives

import Canvas.Canvas.CanvasInterfaceImplementation
import Canvas.Canvas.CanvasLowLevel.Canvas2
import Canvas.Canvas.DrawableCanvas
import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.shape.*
import javafx.scene.text.Font
import java.io.ObjectOutputStream
import java.io.Serializable
import java.sql.Timestamp



abstract class DrawbleObj: InteractableObject, Serializable {
    companion object {
        private const val serialVersionUID: Long = 7987272689064760185
    }
    var editMode = false
    var showOnCanvas = true

    fun display(canvas: Canvas2) {
        if (showOnCanvas) {
            canvas.add(node)
        }
    }
}

class Circle3 (
        var x: Double,
        var y: Double,
        var radius: Double,
        var color: String,
        var stroke: String,
        var strokeWidth: Double,
        var timestamp: Long = Timestamp(System.currentTimeMillis()).time
): DrawbleObj(), Serializable, PointDragDraw {

    companion object {
        private const val serialVersionUID: Long = -1
    }

    @Transient override lateinit var node: Circle

    init {
        init()
    }

    fun init() {
        node = Circle(x, y, radius)
        node.fill = Color.web(color)
        node.strokeWidth = strokeWidth
        node.stroke = Color.web(stroke)

        node.centerXProperty().addListener { observable, oldValue, newValue ->
            x = newValue as Double
        }
        node.centerYProperty().addListener { observable, oldValue, newValue ->
            y = newValue as Double
        }
        node.radiusProperty().addListener { observable, oldValue, newValue ->
            radius = newValue as Double
        }
        node.fillProperty().addListener { observable, oldValue, newValue ->
            color = newValue.toString()
        }
        node.strokeProperty().addListener { observable, oldValue, newValue ->
            stroke = newValue.toString()
        }
        node.strokeWidthProperty().addListener { observable, oldValue, newValue ->
            strokeWidth = newValue as Double
        }
    }

    override fun setStart(x: Double, y: Double) {
        node.centerX = x
        node.centerY = y
    }

    override fun setEnd(x: Double, y: Double) {
        val p1 = Point2D(node.centerX, node.centerY)
        node.radius = p1.distance(x, y)
    }

    private fun writeObject(stream: ObjectOutputStream) {
        stream.defaultWriteObject()
    }

    private fun readObject(stream: java.io.ObjectInputStream) {
        stream.defaultReadObject()
        init()
    }
}

class Rectangle3 (
        private var startX: Double,
        private var startY: Double,
        private var width: Double,
        private var height: Double,
        private var color: String,
        private var stroke: String,
        private var strokeWidth: Double,
        private var timestamp: Long = Timestamp(System.currentTimeMillis()).time
): DrawbleObj(), Serializable, PointDragDraw {

    companion object {
        private const val serialVersionUID: Long = -1
    }

    @Transient override lateinit var node: Rectangle

    init {
        init()
    }

    fun init() {
        node = Rectangle(startX, startY, width, height)
        node.fill = Color.web(color)
        node.strokeWidth = strokeWidth
        node.stroke = Color.web(stroke)

        node.xProperty().addListener { observable, oldValue, newValue ->
            startX = newValue as Double
        }
        node.yProperty().addListener { observable, oldValue, newValue ->
            startY = newValue as Double
        }
        node.widthProperty().addListener { observable, oldValue, newValue ->
            width = newValue as Double
        }
        node.heightProperty().addListener { observable, oldValue, newValue ->
            height = newValue as Double
        }
        node.fillProperty().addListener { observable, oldValue, newValue ->
            color = newValue.toString()
        }
        node.strokeProperty().addListener { observable, oldValue, newValue ->
            stroke = newValue.toString()
        }
        node.strokeWidthProperty().addListener { observable, oldValue, newValue ->
            strokeWidth = newValue as Double
        }
    }

    override fun setStart(x: Double, y: Double) {
        node.x = x
        node.y = y
    }

    override fun setEnd(x: Double, y: Double) {
        val topLeft = Point2D(node.x, node.y)
        val width = x - topLeft.x
        val height = y - topLeft.y
        node.width = width
        node.height = height
    }

    private fun writeObject(stream: ObjectOutputStream) {
        stream.defaultWriteObject()
    }

    private fun readObject(stream: java.io.ObjectInputStream) {
        stream.defaultReadObject()
        init()
    }
}

class Line3 (
        var startX: Double,
        var startY: Double,
        var endX: Double,
        var endY: Double,
        var color: String,
        var stroke: String,
        var strokeWidth: Double,
        var timestamp: Long = Timestamp(System.currentTimeMillis()).time
): DrawbleObj(), Serializable, PointDragDraw {

    companion object {
        private const val serialVersionUID: Long = -1
    }

    @Transient override lateinit var node: Line

    init {
        init()
    }

    fun init() {
        node = Line(startX, startY, endX, endY)
        node.fill = Color.web(color)
        node.strokeWidth = strokeWidth
        node.stroke = Color.web(stroke)
        node.strokeLineCap = StrokeLineCap.ROUND
        node.strokeLineJoin = StrokeLineJoin.ROUND

        node.startXProperty().addListener { observable, oldValue, newValue ->
            startX = newValue as Double
        }
        node.startYProperty().addListener { observable, oldValue, newValue ->
            startY = newValue as Double
        }
        node.endXProperty().addListener { observable, oldValue, newValue ->
            endX = newValue as Double
        }
        node.endYProperty().addListener { observable, oldValue, newValue ->
            endY = newValue as Double
        }
        node.fillProperty().addListener { observable, oldValue, newValue ->
            color = newValue.toString()
        }
        node.strokeProperty().addListener { observable, oldValue, newValue ->
            stroke = newValue.toString()
        }
        node.strokeWidthProperty().addListener { observable, oldValue, newValue ->
            strokeWidth = newValue as Double
        }

        node.isSmooth = false
    }

    override fun setStart(x: Double, y: Double) {
        node.startX = x
        node.startY = y
    }

    override fun setEnd(x: Double, y: Double) {
        node.endX = x
        node.endY = y
    }

    private fun writeObject(stream: ObjectOutputStream) {
        stream.defaultWriteObject()
    }

    private fun readObject(stream: java.io.ObjectInputStream) {
        stream.defaultReadObject()
        init()
    }
}

class Image3 (
        private var name: String,
        private var url: String,
        private var scale: Double,
        private var rotation: Double,
        private var x: Double,
        private var y: Double,
        var data: String,
        var nodeEventType: String,
        imageActionInterface: ImageActionInterface,
        private var timestamp: Long = Timestamp(System.currentTimeMillis()).time
): DrawbleObj(), Serializable {

    companion object {
        private const val serialVersionUID: Long = -1
    }

    var markColor: String? = null
    @Transient override lateinit var node: Group
    @Transient lateinit var colorAdjust: ColorAdjust

    init {
        display(imageActionInterface)
    }

    fun getUrl(): String {
        if (nodeEventType == "Image") {
            return CanvasInterfaceImplementation.loadedProject!!.url + "/files/" + url
        } else {
            return data
        }
    }

    fun display(imageActionInterface: ImageActionInterface) {
        initNewVariables()
        initNode()
        initImage(imageActionInterface)
    }

    fun initNewVariables() {
        if (markColor == null) {
            markColor = "0"
        }
        colorAdjust = ColorAdjust(0.0, 0.0, 0.0, 0.0)
    }

    private fun initNode() {
        node = Group()
        node.scaleXProperty().addListener { _, _, newValue -> scale = newValue as Double }
        node.rotateProperty().addListener { _, _, newValue -> rotation = newValue as Double }
        node.layoutXProperty().addListener { _, _, newValue -> x = newValue as Double }
        node.layoutYProperty().addListener { _, _, newValue -> y = newValue as Double }
        scaleTo(scale)
        moveTo(x, y)
        rotateTo(rotation)
    }

    private fun initImage(imageActionInterface: ImageActionInterface) {

        var image = Image(if(nodeEventType == "Image") "file:" + getUrl() else url)
        var imageView = ImageView(image)

        if (markColor.equals("0") || markColor.equals("3")) {
            var temp = Label("Performance mode, hold alt and click to show")
            temp.minWidth = image.width
            temp.minHeight = image.height
            temp.font = Font("Arial", 25.0)
            temp.padding = Insets(5.0, 5.0, 5.0, 5.0)
            temp.onMouseClicked = EventHandler {
                if (it.isAltDown) {
                    node.children.remove(temp)
                    node.children.add(0, imageView)
                }
            }
            temp.alignment = Pos.CENTER
            temp.background = Background(BackgroundFill(Color.BLACK.deriveColor(1.0, 1.0, 1.0, 0.03), CornerRadii(10.0), Insets.EMPTY))
            node.children.add(temp)
        } else {
            node.children.add(imageView)
        }



        var lbl = Label(name)
        lbl.minWidth = image.width
        lbl.minHeight = 40.0
        lbl.font = Font("Arial", 25.0)
        lbl.padding = Insets(5.0, 5.0, 5.0, 5.0)
        lbl.alignment = Pos.CENTER
        lbl.translateY += image.height
        lbl.background = Background(BackgroundFill(Color.BLACK.deriveColor(1.0, 1.0, 1.0, 0.05), CornerRadii(10.0), Insets.EMPTY))
        NodeGestures(this, DrawableCanvas.canvasInterface)

        imageView.effect = colorAdjust
        setMarkColor()
        node.children.add(btn(image.height + 40.0, -10.0, "Open", imageActionInterface.open(this)))
        node.children.add(btn(image.height + 40.0, 40.0, "Delete", imageActionInterface.delete(this)))
        node.children.add(btn(image.height + 40.0, 90.0, "Mark", EventHandler {
            toggleMarkColor()
            setMarkColor()
        }))
        node.children.add(btn(image.height + 40.0, 140.0, "toString", EventHandler { println("markcolor $markColor data $data nodeEventType $nodeEventType timestamp $timestamp") }))
        node.children.add(lbl)
    }

    private fun setMarkColor() {
        if (markColor.equals("0")) {
            colorAdjust.saturation = 0.0
        } else {
            colorAdjust.saturation = 0.06
        }
        when {
            markColor.equals("0") -> {}
            markColor.equals("1") -> colorAdjust.setHue(0.0)
            markColor.equals("2") -> colorAdjust.setHue(0.6)
            markColor.equals("3") -> colorAdjust.setHue(1.0)
        }
    }
    private fun toggleMarkColor() {
        when {
            markColor.equals("0") -> markColor = "1"
            markColor.equals("1") -> markColor = "2"
            markColor.equals("2") -> markColor = "3"
            markColor.equals("3") -> markColor = "0"
        }
    }

    private fun btn(y: Double, x: Double, name: String, eventHandler: EventHandler<MouseEvent>): Node {
        val btn = JFXButton(name)
        btn.styleClass.addAll("animated-option-button")
        btn.buttonType = JFXButton.ButtonType.RAISED
        btn.onMouseClicked = eventHandler
        btn.scaleX = 0.5
        btn.scaleY = 0.5
        btn.translateY += y
        btn.translateX = x
        return btn
    }

    private fun writeObject(stream: ObjectOutputStream) {
        stream.defaultWriteObject()
    }

    private fun readObject(stream: java.io.ObjectInputStream) {
        stream.defaultReadObject()
        display(DrawableCanvas.canvasInterface)
    }
}

interface ImageActionInterface {
    fun open(image: Image3): EventHandler<MouseEvent>
    fun delete(image: Image3): EventHandler<MouseEvent>
}

class DrawbleObjs(var list: MutableList<DrawbleObj> = ArrayList()): Serializable {

    companion object {
        @Transient @JvmStatic var selected: DrawbleObj? = null
        private const val serialVersionUID: Long = -1
    }
}

interface InteractableObject {

    fun moveTo(newX: Double, newY: Double) {
        node.layoutX = newX
        node.layoutY = newY
    }

    fun scaleTo(newScale: Double) {
        node.scaleX = newScale
        node.scaleY = newScale
    }

    fun scaleBy(amount: Double) {
        node.scaleX = node.scaleX * amount
        node.scaleY = node.scaleY * amount
    }

    fun scaleBy(amount: Double, min: Double, max: Double) {
        node.scaleX = Math.max(min, Math.min(max, node.scaleX * amount))
        node.scaleY = Math.max(min, Math.min(max, node.scaleY * amount))
    }

    fun rotateBy(angle: Double) {
        node.rotate += angle
    }

    fun rotateTo(angle: Double) {
        node.rotate = angle
    }

    fun moveBy(x: Double, y: Double) {
        node.layoutX += x
        node.layoutY += y
    }

    val node: Node
}

interface PointDragDraw {
    fun setStart(x: Double, y: Double)
    fun setEnd(x: Double, y: Double)
}