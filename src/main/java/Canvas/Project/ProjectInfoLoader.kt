package Canvas.Project

import Canvas.aPrimitives.DrawbleObjs
import Main.WindowTest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.apache.commons.io.FilenameUtils
import java.io.*
import java.util.logging.Level
import java.util.logging.Logger
import java.util.prefs.Preferences




/**
 * Created by valauskasmodestas on 26.06.17.
 */
class ProjectInfoLoader {

    internal var projectInfos: ArrayList<ProjectInfoFile> = ArrayList()
    internal var PREF_NAME = "MVWhiteboardProjects"

    constructor() {
        projectInfos = getProjectList()
    }

    fun addProject(name: String, url: String) {
        projectInfos.add(ProjectInfoFile(name, url))
    }

    fun add(projectInfoFile: ProjectInfoFile) {
        projectInfos.add(projectInfoFile)
    }

    fun putToPrefs() {
        val mapper = ObjectMapper()
        val prefs = Preferences.userNodeForPackage(ProjectInfoLoader::class.java)
        prefs.put(PREF_NAME, mapper.writeValueAsString(projectInfos))
    }

    fun getProjectList(): ArrayList<ProjectInfoFile> {
        val prefs = Preferences.userNodeForPackage(ProjectInfoLoader::class.java)
        if (!prefs.get(PREF_NAME, "").equals("")) {
            return ArrayList(loadList<ProjectInfoFile>(prefs.get(PREF_NAME, ""), ProjectInfoFile::class.java)
                    .filter { File(it.url).exists() })
        } else {
            return ArrayList()
        }
    }

    fun createNewProject() {
        val fileChooser = FileChooser()
        val extFilter = FileChooser.ExtensionFilter("White folder (*.white)", "*.white")
        fileChooser.extensionFilters.add(extFilter)

        val file = fileChooser.showSaveDialog(WindowTest.instance.stage)

        if (file != null && file.exists()) {
            addProject(FilenameUtils.getName(file.absolutePath), file.absolutePath)
            putToPrefs()
        } else if(!file.exists()) {
            try {
                file.mkdir()
                if (file.isDirectory) {
                    setupWhiteFolder(file)
                    addProject(FilenameUtils.getName(file.absolutePath), file.absolutePath)
                    putToPrefs()
                } else {
                    println("could not create folder")
                }
            } catch (ex: IOException) {
                Logger.getLogger(this::class.java.getName()).log(Level.SEVERE, null, ex)
            }
        }
    }


    fun openProject() {
        val chooser = DirectoryChooser()
        chooser.title = "White Projects"
        val selectedDirectory = chooser.showDialog(WindowTest.instance.stage)
        if (selectedDirectory.exists()) {
            addProject(FilenameUtils.getName(selectedDirectory.absolutePath), selectedDirectory.absolutePath)
            putToPrefs()
        }
    }

    fun setupWhiteFolder(file: File) {
        var configUrl = file.absolutePath + "/data.white"
        try {
            val fileOut = FileOutputStream(file.absolutePath + "/data.white")
            val out = ObjectOutputStream(fileOut)
            out.writeObject(DrawbleObjs())
            out.close()
            fileOut.close()
            System.out.printf("Serialized data is saved ")
        } catch (i: IOException) {
            i.printStackTrace()
        }

        var filesUrl = file.absolutePath + "/" + "files"

        var fileWriter: FileWriter = FileWriter(configUrl)
        fileWriter.write("{}")
        fileWriter.close()

        if (File(filesUrl).mkdir()) {
            println("could not create white data folder")
        }
    }

    fun <T> loadList(json: String, type: Class<*>): ArrayList<T> {
        val mapper = ObjectMapper()
        var list = ArrayList<T>()
        try {
            val t = TypeFactory.defaultInstance()
            list = mapper.readValue(json, t.constructCollectionType(List::class.java, type))
        } catch (e: IOException) {
            println("couldnt read json message: " + e.message)
        }

        return list
    }
}