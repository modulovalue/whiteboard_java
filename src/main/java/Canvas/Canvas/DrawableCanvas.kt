package Canvas.Canvas

import Canvas.Canvas.CanvasLowLevel.Canvas2
import Canvas.Canvas.CanvasLowLevel.CustomContainer.JFXHNodesList
import Canvas.Canvas.CanvasLowLevel.CustomContainer.JFXVNodesList
import Canvas.Interfaces.CanvasInterface
import Canvas.Interfaces.DragAndDropInterface
import Canvas.Interfaces.SelectableInterface
import Canvas.Project.ProjectInfoFile
import Canvas.Tool.DrawableTool
import Canvas.Tool.PointDragDrawToolFactory
import Canvas.Tool.ToolEraser
import Canvas.Tool.ToolPen
import Canvas.aPrimitives.*
import Main.WindowTest
import com.google.gson.Gson
import com.jfoenix.controls.JFXButton
import com.jfoenix.effects.JFXDepthManager
import com.sun.javafx.perf.PerformanceTracker
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.awt.Desktop
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

typealias V = JFXVNodesList
typealias H = JFXHNodesList

class DrawableCanvas constructor(interactionNode: Node?) {

    private val canvas: Canvas2
    private var tool: DrawableTool
    private var dragAndDrop: DragAndDrop? = null

    companion object {
        @JvmStatic lateinit var canvasInterface: CanvasInterfaceImplementation
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("Viewfxml/CanvasShell.fxml"))
        fxmlLoader.load<Any>()
        canvas = fxmlLoader.getController<Canvas2>()
        if (interactionNode == null) {
            canvas.interactionNode = outermostContainer
        } else {
            canvas.interactionNode = interactionNode
        }
        canvasInterface = CanvasInterfaceImplementation(canvas)
        initButtons()

        tool = ToolPen(1.0, 7.0)
        tool.setToolActive(canvasInterface)
    }

    fun initButtons() {
        canvas.toolGridPane.add(V(10.0).apply {
            add(H(10.0).apply {
                add(btn("Select", { println("todo select") }))
            })
            add(H(10.0).apply {
                add(btnOuter("Draw"))
                add(btn("Erase") { setTool(this, ToolEraser(20.0, null)) })
                add(btn("Pen") { setTool(this, ToolPen(1.0, 7.0)) })
                add(V(10.0).addBtns(
                        btn("Line") { setTool(this, PointDragDrawToolFactory("Line", Color.RED, Color.BLACK, 3.0)) }
                        , btn("Marker") { setTool(this, PointDragDrawToolFactory("Line", Color.RED, Color.BLACK, 3.0)) }
                        , btn("Erase Line") { setTool(this, ToolEraser(20.0, Line3.javaClass)) }
                ))
                add(V(10.0).addBtns(
                        btn("Circle") { setTool(this, PointDragDrawToolFactory("Circle", Color.TRANSPARENT, Color.RED, 3.0)) }
                        , btn("Erase Circle") { setTool(this, ToolEraser(20.0, Circle3.javaClass)) }
                        //, btn("Ring") { setTool(this, PointDragDrawToolFactory("Circle", Color.RED, Color.TRANSPARENT, 8.0)) }
                ))
                add(V(10.0).addBtns(
                        btn("Rect") { setTool(this, PointDragDrawToolFactory("Rectangle", Color.TRANSPARENT, Color.RED, 8.0)) }
                        , btn("Erase Rect") { setTool(this, ToolEraser(20.0, Rectangle3.javaClass)) }
                        //, btn("Frame") { setTool(this, PointDragDrawToolFactory("Rectangle", Color.RED, Color.TRANSPARENT, 3.0)) }
                ))
            })
            add(H(10.0).apply {
                add(btn("Save") { canvasInterface.save() })
            })
            add(H(10.0).apply {
                add(btn("Load") { canvasInterface.load() })
            })
            add(H(10.0).apply {
                add(btn("FPS") {
                    PerformanceTracker.getSceneTracker(WindowTest.instance.rootScene).resetAverageFPS()
                    println("instantfps " + PerformanceTracker.getSceneTracker(WindowTest.instance.rootScene).instantFPS + "  average fps " + PerformanceTracker.getSceneTracker(WindowTest.instance.rootScene).averageFPS)})
            })
            add(H(10.0).apply {
                add(btn("gson") {
                    canvasInterface.saveJSON()
                })
            })
            animateList()
        }, 0, 0)
    }


    fun btnOuter(name: String): JFXButton {
        val btn = JFXButton(name)
        btn.styleClass.addAll("animated-option-button-option")
        btn.buttonType = JFXButton.ButtonType.RAISED
        return btn
    }

    fun btn(name: String, event: (MouseEvent) -> Unit): JFXButton {
        val btn = JFXButton(name)
        btn.styleClass.addAll("animated-option-button")
        btn.buttonType = JFXButton.ButtonType.RAISED
        btn.onMouseClicked = EventHandler<MouseEvent> { event(it) }
        return btn
    }

    fun setupDragAndDrop(scene: Scene) {
        dragAndDrop = DragAndDrop(scene, canvasInterface)
    }

    val outermostContainer: Pane
        get() = canvas.outerGridPane

    fun setTool(list: JFXHNodesList? = null, tool: DrawableTool) {
        list?.animateList()
        canvasInterface.selected(null)
        if (this.tool != null) {
            this.tool!!.setToolInactive()
        }
        this.tool = tool
        this.tool!!.setToolActive(canvasInterface)
    }

    fun setVisible(b: Boolean) {
        canvas.outerGridPane.isVisible = b
    }

    fun loadProject(projectInfoFile: ProjectInfoFile) {
        CanvasInterfaceImplementation.loadedProject = projectInfoFile
        canvasInterface.load()
    }

    fun zoomOut() {
        canvas.zoomOut()
    }
}



class CanvasInterfaceImplementation(var canvas: Canvas2) : CanvasInterface, DragAndDropInterface, SelectableInterface, ImageActionInterface {

    internal var objects = DrawbleObjs()

    companion object {
        var loadedProject: ProjectInfoFile? = null
    }

    fun canvasPoint(e: DragEvent): Point2D {
        return canvas.canvasPane.sceneToLocal(Point2D(e.sceneX, e.sceneY))
    }

    // canvas interface
    override fun addNewObject(obj: DrawbleObj) {
        objects.list.add(obj)
        DrawbleObjs.selected = obj
        canvas.add(obj.node)
    }

    override fun erase(point: Point2D, radius: Double, aClass: Class<Any>) {
        if (aClass == null) {

        } else {
            var list = ArrayList<DrawbleObj>()

            objects.list.forEach {
//                if (aClass.equals(it.javaClass)) {
//                    var dst: Double = 100.0
//                    if (it is Circle3) {
//                        dst = Line2D.ptLineDist(it.x, it.y, it.x, it.y, point.x, point.y)
//                    } else if (it is Line)
//
//                    if (dst < radius) {
//                        list.add(it)
//                    }
//                }
            }

            objects.list.removeAll(list)
            list.forEach {
                if (it.node is Node) {
                    canvas.canvasPane.children.remove(it.node)
                }
            }
        }
    }

    fun save() {
        try {
            val fileOut = FileOutputStream(loadedProject!!.url+"/data.white")
            val out = ObjectOutputStream(fileOut)
            out.writeObject(objects)
            out.close()
            fileOut.close()
            System.out.printf("Serialized data is saved ")
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }

    fun saveJSON() {
        var json = Gson().toJson(objects)
        println(json)
        try {
            var out = PrintWriter(loadedProject!!.url+"/datajson.white");
            out.println(json)
            out.close()
            System.out.printf("Serialized data is saved to json")
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }

    fun load() {
        try {
            var fileIn = FileInputStream(loadedProject!!.url+"/data.white")
            var inStream = ObjectInputStream(fileIn)
            objects = inStream.readObject() as DrawbleObjs

            inStream.close()
            fileIn.close()
            canvas.canvasPane.children.clear()

            objects.list.forEach {
                it.showOnCanvas = true
                it.display(canvas)
            }
        } catch(e: IOException) {
            e.printStackTrace()
        } catch(e: ClassNotFoundException) {
            System.out.println("class not found")
            e.printStackTrace()
        }
    }

    // Drag and drop interface
    override fun newImage(file: File, e: DragEvent) {
        try {
            var copied = copyImage(file)
            val image = ObjectFactory.getImg(copied.first, canvasPoint(e), file.absolutePath, "Image", this, FilenameUtils.getName(copied.second.absolutePath))
            addNewObject(image)
        } catch (exception: MalformedURLException) {
            println("could not load image")
        }
    }

    override fun newDirectory(file: File, e: DragEvent) {
        val directory = ObjectFactory.getImg(FilenameUtils.getName(file.absolutePath), canvasPoint(e), file.absolutePath, "Directory", this)
        addNewObject(directory)
    }

    override fun newLink(link: String, e: DragEvent) {
        val dl = ObjectFactory.getImg(link, canvasPoint(e), link, "Link", this)
        addNewObject(dl)
    }

    fun copyImage(file: File): Pair<String, File> {
        var oldName = FilenameUtils.getName(file.absolutePath)
        var newUrl = loadedProject!!.url + "/files/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(oldName)
        var newFile = File(newUrl)
        FileUtils.moveFile(file, newFile)
        return Pair(oldName, newFile)
    }

    // Selectable interface
    override fun selected(obj: DrawbleObj?) {
        if (DrawbleObjs.selected != null) {
            JFXDepthManager.setDepth(DrawbleObjs.selected!!.node, 0)
            DrawbleObjs.selected = null
        }
        DrawbleObjs.selected = obj
        if (obj != null) {
            JFXDepthManager.setDepth(obj.node, 2)
        }
    }

    override fun resizeMode(b: Boolean) {
        canvas.canvasPane.background = Background(BackgroundFill(if (b) Color.DARKGRAY else Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
    }

    override fun getDrawingPane(): Pane = canvas.canvasPane

    // ImageActionInterface
    override fun open(image: Image3): EventHandler<MouseEvent> {
        return EventHandler {
            when  {
                image.nodeEventType.equals("Link") -> {
                    val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(URL(image.data).toURI())
                    }
                }
                image.nodeEventType.equals("Directory") ||
                image.nodeEventType.equals("Image")  -> {
                    Desktop.getDesktop().open(File(image.getUrl()))
                }
            }
        }
    }

    override fun delete(image: Image3): EventHandler<MouseEvent> {
        return EventHandler {
            image.showOnCanvas = false
            canvas.canvasPane.children.remove(image.node)
        }
    }
}