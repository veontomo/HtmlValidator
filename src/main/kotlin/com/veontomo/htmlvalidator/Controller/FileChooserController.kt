package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.GUI
import javafx.stage.FileChooser
import javafx.stage.Stage

/**
 * Controller for choosing and selecting files for further analysis.
 */
class FileChooserController(val stage: Stage, val view: GUI) {
    fun onSelectBtnClick() {
        val allowedExtensions = listOf("html", "htm")
        val fileChooser = FileChooser()
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
            }
        } else {
            view.enableAalyzeBtn(false)
        }
    }

}