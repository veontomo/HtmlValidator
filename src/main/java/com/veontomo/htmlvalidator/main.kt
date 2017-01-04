package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Controller.Controller

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.Stage

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
    val checkersListView = ListView<String>()

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

        grid.add(hbBtn, 1, 1)
        grid.add(fileNameText, 1, 2)
        grid.add(browser, 0, 3, 10, 10)
        grid.add(checkersListView, 14, 3, 3, 10)
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
    fun loadItems(items: List<String>) {
        checkersListView.items = FXCollections.observableArrayList(items)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }

}

