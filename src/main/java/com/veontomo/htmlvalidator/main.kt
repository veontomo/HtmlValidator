package com.veontomo.htmlvalidator

import java.io.File

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
    val attrPlain = setOf("title", "href", "width", "height", "alt", "src", "style", "target", "http-equiv", "content", "cellpadding", "cellspacing")
    val attrInline = setOf(
            "width", "max-width", "min-width",
            "padding", "padding-top", "padding-bottom", "padding-left", "padding-right",
            "margin", "margin-top", "margin-bottom", "margin-left", "margin-right",
            "text-decoration", "text-align", "line-height",
            "font-size", "font-weight", "font-family", "font-style",
            "border", "border-style", "border-spacing",
            "color", "height",
            "display", "vertical-align", "background-color"
    )
    val charsets = setOf("ascii")

    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(),
            PlainAttrChecker(attrPlain), InlineAttrChecker(attrInline), EncodingChecker(charsets))
    if (args.isNotEmpty()) {
        val folder = args[0]
        println("Processing files from folder $folder")
        val directory = File(folder)
        val fList = directory.listFiles()
        for (file in fList) {
            println("Checking file ${file.name}")
            val reports = runCheckers(file, checkers)
            for ((name, report) in reports) {
                if (report.isEmpty()) {
                    println("$name: OK")
                } else {
                    println("$name: ${report.size} message(s):")
                    println(report.mapIndexed { i, it -> "${i+1}. ${it.message}\n" }.joinToString("", "", "", -1, "", { it }))
                }
            }
        }
    } else {
        println("No folder has been given hence no file has been processed.")
    }
}

/**
 * Check given file against the checkers.
 * Return a list of messages related to each checker grouped by the checker.
 */
fun runCheckers(file: File, checkers: List<Checker>): Map<String, List<CheckMessage>> {
    val text = file.readText()
    return checkers.associateBy({ it.descriptor }, { it.check(text) })
}

class GUI : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.setTitle("JavaFX Welcome")
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
        btn.setOnAction {e ->
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in button pressed");

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

