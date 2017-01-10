package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Controller.AnalyzerController
import com.veontomo.htmlvalidator.Controller.FileChooserController
import com.veontomo.htmlvalidator.Models.Report

import javafx.application.Application
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.image.Image
import javafx.scene.input.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.io.File
import java.security.Key

/**
 * Entry point
 */
fun main(args: Array<String>) {
    GUI.main(args)
}

/**
 * A GUI application that performs various checks of selected html file.
 */
class GUI : Application() {
    val fileNameText = Text()
    val browser = WebView()
    val checkersView = TableView<Report>()
    val checkerNameCol = TableColumn<Report, String>("Checker")
    val checkerStatusCol = TableColumn<Report, String>("Status")
    val checkerCommentCol = TableColumn<Report, String>("Comment")
    var analyzerController: AnalyzerController? = null
    var fileChooserController: FileChooserController? = null
    // keyboard shortcut for selecting a file "Ctrl+o"
    val fileSelectShortcut = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
    // keyboard shortcut for analyzing a selected file "Ctrl+a"
    val analyzeShortcut = KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)
    // keyboard shortcut for clearing the results
    val clearShortcut = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)
    val menuSelect = MenuItem("Select file")
    val menuAnalyze = MenuItem("Analyze file")
    val menuClear = MenuItem("Clear")

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Html validator"
        primaryStage.icons.add(Image("/logo.png"))
        val grid = GridPane()
        grid.alignment = Pos.TOP_LEFT
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(0.0, 10.0, 10.0, 10.0)

        checkerNameCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerNameCol.setCellValueFactory { data -> ReadOnlyStringWrapper(data.value.name) }
        checkerStatusCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerStatusCol.setCellValueFactory { data -> ReadOnlyStringWrapper(if (data.value.status) "OK" else "Fail") }
        checkerStatusCol.maxWidth = 40.0
        checkerNameCol.prefWidthProperty().bind(checkersView.widthProperty().multiply(0.3))
        checkerCommentCol.prefWidthProperty().bind(checkersView.widthProperty().multiply(0.5))
        checkerCommentCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerCommentCol.setCellValueFactory { data -> ReadOnlyStringWrapper(data.value.comment) }

        checkersView.columns.addAll(checkerNameCol, checkerStatusCol, checkerCommentCol)
        val checkerWidth = 1
        val browserWidth = 1
        grid.add(checkersView, 0, 1, checkerWidth, 10)
        grid.add(fileNameText, 0, 11)
        grid.add(browser, 0, 12, browserWidth, 10)
        val menuBar = MenuBar()
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty())
        val menuFile = Menu("File")
        val menuInfo = Menu("?")
        menuFile.items.addAll(menuSelect, menuAnalyze, menuClear)
        menuAnalyze.isDisable = true
        menuBar.menus.addAll(menuFile, menuInfo)
        grid.children.add(menuBar)
        val scene = Scene(grid, primaryStage.width - grid.padding.left - grid.padding.right, 500.0)

        primaryStage.scene = scene
        primaryStage.show()
        analyzerController = AnalyzerController(primaryStage, this)
        fileChooserController = FileChooserController(primaryStage, this)
        menuSelect.setOnAction { fileChooserController?.onSelect() }
        menuAnalyze.setOnAction { analyzerController?.onAnalyze() }
        menuClear.setOnAction { analyzerController?.onClear() }
        menuSelect.accelerator = fileSelectShortcut
        menuAnalyze.accelerator = analyzeShortcut
        menuClear.accelerator = clearShortcut
        scene.addEventHandler(KeyEvent.KEY_RELEASED, { event -> if (fileSelectShortcut.match(event)) fileChooserController?.onSelect() })
        scene.addEventHandler(KeyEvent.KEY_RELEASED, { event -> if (analyzeShortcut.match(event)) analyzerController?.onAnalyze() })
        scene.addEventHandler(KeyEvent.KEY_RELEASED, { event -> if (clearShortcut.match(event)) analyzerController?.onClear() })
    }

    /**
     * Display the selected file name
     * @param name file name
     */
    fun showFileName(name: String) {
        fileNameText.text = name
    }

    /**
     * Display the content of the selected file inside the web viewer.
     * @param url
     */
    fun showFileContent(url: String) {
        browser.engine.load(url)
    }

    /**
     * Enable/disable a menu item that is used to analyze the file
     * @param isEnabled true to enable, false to disable
     */
    fun enableAnalyze(isEnabled: Boolean) {
        menuAnalyze.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to select a file
     * @param isEnabled true to enable, false to disable
     */
    fun enableSelect(isEnabled: Boolean) {
        menuSelect.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to clear the result of analysis
     * @param isEnabled true to enable, false to disable
     */
    fun enableClear(isEnabled: Boolean) {
        menuClear.isDisable = !isEnabled
    }

    /**
     * Load the items in to the list view
     * @param items
     */
    fun loadItems(items: List<Report>) {
        checkersView.items = FXCollections.observableArrayList(items)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }

    /**
     * Inform the appropriate controller that the user has selected a file.
     * @param file
     */
    fun onFileSelected(file: File) {
        analyzerController?.setFile(file)
    }

}

