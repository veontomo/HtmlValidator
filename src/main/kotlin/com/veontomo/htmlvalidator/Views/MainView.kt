package com.veontomo.htmlvalidator.Views

import com.veontomo.htmlvalidator.Controller.AnalyzerController
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
//    var fileChooserController: FileChooserController? = null

    fun getScene(): Scene {
        val grid = FXMLLoader.load<GridPane>(javaClass.getResource("/MainView.fxml"))
        val scene = Scene(grid, stage.width - grid.padding.left - grid.padding.right, 500.0)
        return scene
    }

}


