package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.*
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

/**
 * A controller that orchestrates execution of available checks of given file.
 */
class Controller(val stage: Stage) {

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

    private fun performCheck() {
        val folder = "data"
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
                    println(report.mapIndexed { i, it -> "${i + 1}. ${it.message}\n" }.joinToString("", "", "", -1, "", { it }))
                }
            }
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

    fun onClick() {
        val fileChooser = FileChooser()
        fileChooser.title = "Select file"
        val file = fileChooser.showOpenDialog(stage)
        if (file != null) {
            println("file is chosen")
        } else
            println("no file is chosen")
    }
}

