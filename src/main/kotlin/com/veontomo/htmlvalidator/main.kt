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
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.io.File

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
    val selectBtn = Button("Select file")
    val analyzeBtn = Button("Analyze")
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

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Html validator"
        primaryStage.icons.add(Image("/logo.png"))
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        val hbBtn = HBox(10.0)
        hbBtn.alignment = Pos.BOTTOM_LEFT
        hbBtn.children.addAll(selectBtn, analyzeBtn)

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
        checkersView.minWidth = 500.0
        val checkerWidth = 30
        val browserWidth = 30
        grid.add(checkersView, 0, 0, checkerWidth, 10)
        grid.add(fileNameText, 0, 10)
        grid.add(browser, 0, 11, browserWidth, 10)
        grid.add(hbBtn, checkerWidth + 1, 0)
        val scene = Scene(grid, 1000.0, 800.0)

        primaryStage.scene = scene
        primaryStage.show()
        analyzerController = AnalyzerController(primaryStage, this)
        fileChooserController = FileChooserController(primaryStage, this)
        selectBtn.setOnAction { fileChooserController?.onSelectBtnClick() }
        analyzeBtn.setOnAction { analyzerController?.onAnalyzeBtnClick() }
        scene.addEventHandler(KeyEvent.KEY_RELEASED, { event -> if (fileSelectShortcut.match(event)) fileChooserController?.onSelectBtnClick() })
        scene.addEventHandler(KeyEvent.KEY_RELEASED, { event -> if (analyzeShortcut.match(event)) analyzerController?.onAnalyzeBtnClick() })
    }

    /**
     * Display the selected file name
     * @param name file name
     */
    fun showFileName(name: String) {
        fileNameText.text = name
        fileNameText.wrappingWidth = 0.75*browser.width
    }

    /**
     * Display the content of the selected file inside the web viewer.
     * @param url
     */
    fun showFileContent(url: String) {
        browser.engine.load(url)
    }

    /**
     * Enable/disable the analyze button
     * @param isEnabled status of the button: true to enable, false to disable
     */
    fun enableAalyzeBtn(isEnabled: Boolean) {
        analyzeBtn.isDisable = !isEnabled
    }

    /**
     * Enable/disable the button that is used to select a file
     * @param isEnabled status of the button: true to enable, false to disable
     */
    fun enableSelectBtn(isEnabled: Boolean) {
        selectBtn.isDisable = !isEnabled
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

