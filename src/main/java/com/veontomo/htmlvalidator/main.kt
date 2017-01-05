package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Controller.Controller
import com.veontomo.htmlvalidator.Models.Report

import javafx.application.Application
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.StringProperty
import javafx.beans.property.StringPropertyBase
import javafx.beans.property.adapter.JavaBeanStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.Stage
import javafx.util.Callback

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


    override fun start(primaryStage: Stage) {
        primaryStage.title = "Html validator"
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        val hbBtn = HBox(10.0)
        hbBtn.alignment = Pos.BOTTOM_RIGHT
        hbBtn.children.addAll(selectBtn, analyzeBtn)

        checkerNameCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerNameCol.setCellValueFactory  {data -> ReadOnlyStringWrapper(data.value.name) }
        checkerStatusCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerStatusCol.setCellValueFactory  {data -> ReadOnlyStringWrapper(if (data.value.status) "OK" else "Fail") }
        checkerCommentCol.cellFactory = TextFieldTableCell.forTableColumn()
        checkerCommentCol.setCellValueFactory  {data -> ReadOnlyStringWrapper(data.value.comment) }
        checkersView.columns.addAll(checkerNameCol, checkerStatusCol, checkerCommentCol)
        grid.add(checkersView, 0, 0, 10, 10)
        grid.add(hbBtn, 11, 0)
        grid.add(browser, 11, 1, 10, 10)
        grid.add(fileNameText, 11, 12)
        val scene = Scene(grid, 600.0, 800.0)

        primaryStage.scene = scene
        primaryStage.show()
        val controller = Controller(primaryStage, this)
        selectBtn.setOnAction { controller.onSelectBtnClick() }
        analyzeBtn.setOnAction { controller.onAnalyzeBtnClick() }
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

}

