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
import javafx.stage.Stage

/**
 * A script that makes the checkers run against given files.
 */
fun main(args: Array<String>) {
    GUI.main(args)
}


class GUI : Application() {
    override fun start(primaryStage: Stage) {
        val controller = Controller()
        primaryStage.title = "Html validator"
        val grid = GridPane()
        grid.setAlignment(Pos.CENTER)
        grid.setHgap(10.0)
        grid.setVgap(10.0)
        grid.setPadding(Insets(25.0, 25.0, 25.0, 25.0))
        val scenetitle = Text("Welcome")
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20.0))
        grid.add(scenetitle, 0, 0, 2, 1)

        val actiontarget = Text("hidden")
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20.0))
        grid.add(actiontarget, 0, 4, 2, 1)

        val userName = Label("User Name:")
        grid.add(userName, 0, 1)

        val userTextField = TextField()
        grid.add(userTextField, 1, 1)

        val btn = Button("Sign in")
        val hbBtn = HBox(10.0)
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT)
        hbBtn.getChildren().add(btn)
        grid.add(hbBtn, 1, 4)
        btn.setOnAction { e ->
            controller.onClick()

        }
        val scene = Scene(grid, 300.0, 275.0)
        primaryStage.setScene(scene)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }

}

