package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Controller.AnalyzerController
import com.veontomo.htmlvalidator.Controller.FileChooserController
import com.veontomo.htmlvalidator.Models.Report
import com.veontomo.htmlvalidator.Views.MainView

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

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Html validator"
        primaryStage.icons.add(Image("/logo.png"))

        primaryStage.scene = MainView(primaryStage).getScene()
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }



}

