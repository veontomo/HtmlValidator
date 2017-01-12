package com.veontomo.htmlvalidator.Views

import com.veontomo.htmlvalidator.Controller.AnalyzerController
import com.veontomo.htmlvalidator.Controller.FileChooserController
import com.veontomo.htmlvalidator.Models.Report
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.io.File

/**
 * Main view
 */
class MainView(val stage: Stage) {

    val checkerNameCol = TableColumn<Report, String>("Checker")
    val checkerStatusCol = TableColumn<Report, String>("Status")
    val checkerCommentCol = TableColumn<Report, String>("Comment")
    var analyzerController = AnalyzerController()
    var fileChooserController: FileChooserController? = null

    fun getScene(): Scene {
        val grid = FXMLLoader.load<GridPane>(javaClass.getResource("/MainView.fxml"))
        val scene = Scene(grid, stage.width - grid.padding.left - grid.padding.right, 500.0)
        return scene
    }

    /**
     * Display the selected file name
     * @param name file name
     */
    fun showFileName(name: String) {
//        fileNameText.text = name
    }

    /**
     * Display file info
     * @param data information to display
     */
    fun showFileInfo(data: String) {
//        fileInfoText.text = if (!data.isBlank()) "Last modified: $data" else null
    }

    /**
     * Display the content of the selected file inside the web viewer.
     * @param url
     */
    fun showFileContent(url: String?) {
//        browser.engine.load(url)
    }

    /**
     * Enable/disable a menu item that is used to analyze the file
     * @param isEnabled true to enable, false to disable
     */
    fun enableAnalyze(isEnabled: Boolean) {
//        menuAnalyze.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to select a file
     * @param isEnabled true to enable, false to disable
     */
    fun enableSelect(isEnabled: Boolean) {
//        menuSelect.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to clear the result of analysis
     * @param isEnabled true to enable, false to disable
     */
    fun enableClear(isEnabled: Boolean) {
//        menuClear.isDisable = !isEnabled
    }

    /**
     * Load the items in to the list view
     * @param items
     */
    fun loadItems(items: List<Report>) {
//        checkersView.items = FXCollections.observableArrayList(items)
    }

    /**
     * Inform the appropriate controller that the user has selected a file.
     * @param file
     */
    fun onFileSelected(file: File) {
//        analyzerController?.setFile(file)
    }
}