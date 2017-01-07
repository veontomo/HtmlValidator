package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.GUI
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

/**
 * Controller for choosing and selecting files for further analysis.
 */
class FileChooserController(val stage: Stage, val view: GUI) {
    /**
     * Name of the file that stores the preferences
     */
    val pref = "HtmlValidator.history"

    fun onSelectBtnClick() {
        val allowedExtensions = listOf("html", "htm")
        val last = readLastUsedFileName()
        val fileChooser = FileChooser()
        if (last != null) {
            val file = File(last)
            if (file.exists() && file.isDirectory)
            fileChooser.initialDirectory = file
        }
        fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("html", allowedExtensions.map { "*.$it" })
        )
        fileChooser.title = "Select a file"
        val file = fileChooser.showOpenDialog(stage)

        if (file?.exists() ?: false) {
            if (allowedExtensions.contains(file.extension)) {
                view.showFileName(file!!.path)
                view.showFileContent(file!!.toURI().toURL().toExternalForm())
                view.enableAalyzeBtn(true)
                view.onFileSelected(file)
                saveLastUsedDir(file!!.parent)
            }
        } else {
            view.enableAalyzeBtn(false)
        }
    }

    /**
     * A folder from which a file that has been chosen last time.
     * @return name of a folder or null.
     */
    private fun readLastUsedFileName(): String? {
        val prefFile = File(pref)
        if (prefFile.exists()) {
             return prefFile.readText()
        }
        return null
    }

    /**
     * Save folder name where the file is located.
     * @param dirName folder name
     */
    private fun saveLastUsedDir(dirName: String) {
        val file = File(pref)
        file.writeText(dirName)
    }

}