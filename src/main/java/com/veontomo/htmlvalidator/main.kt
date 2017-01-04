package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Controller.Controller

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Stage

/**
 * A script that makes the checkers run against given files.
 */
fun main(args: Array<String>) {
    GUI.main(args)
}


class GUI : Application() {
    val fileNameText = Text()

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Html validator"
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)
        grid.add(fileNameText, 2, 2, 1, 1)
        val sceneTitle = Text("Welcome")
        sceneTitle.font = Font.font("Tahoma", FontWeight.NORMAL, 20.0)
        grid.add(sceneTitle, 0, 0, 2, 1)


        val userName = Label("File")
        grid.add(userName, 0, 1)

        val userTextField = TextField()
        grid.add(userTextField, 1, 1)

        val btn = Button("Select file")
        val hbBtn = HBox(10.0)
        hbBtn.alignment = Pos.BOTTOM_RIGHT
        hbBtn.children.add(btn)
        grid.add(hbBtn, 1, 4)
        val scene = Scene(grid, 300.0, 275.0)

        primaryStage.scene = scene
        primaryStage.show()
        val controller = Controller(primaryStage, this)
        btn.setOnAction { e ->
            controller.onClick()
        }
    }

    fun showFileName(name: String){
       fileNameText.text = name
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }

}

