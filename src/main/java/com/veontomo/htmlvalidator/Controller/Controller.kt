package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.*
import com.veontomo.htmlvalidator.Models.Report
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

/**
 * A controller that orchestrates execution of available checks of given file.
 */
class Controller(val stage: Stage, val view: GUI) {

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

    var selectedFile: File? = null

    init {
        view.enableSelectBtn(true)
        view.enableAalyzeBtn(false)
        view.loadItems(checkers.map { Report(it.descriptor, true, "") })
    }

    private fun performCheck(file: File) {
        val text = file.readText()
        view.loadItems(checkers.map { val messages = it.check(text); if (messages.isEmpty()) Report(it.descriptor, true, "") else createReport(messages) })
    }

    /**
     * Merge check messages into a single report.
     * Assume that all check messages originate from the same checker and that the list contains at least one
     * message.
     * The status of the resulting report instance is to be true if and only if statuses of all
     * check messages are true.
     * @param messages list of messages. Require that the list contains at least one message and all messages have
     * the same origin.
     * @return a report summarizing the check messages
     */
    private fun createReport(messages: List<CheckMessage>): Report {
        val origin = messages[0].origin
        val status = messages.all { it.status }
        val summary = messages.joinToString { it.msg }
        return Report(origin, status, summary)
    }

    fun onSelectBtnClick() {
        val fileChooser = FileChooser()
        fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("html", "*.html"),
                FileChooser.ExtensionFilter("htm", "*.htm")
        )
        fileChooser.title = "Select a file"
        selectedFile = fileChooser.showOpenDialog(stage)
        if (selectedFile?.exists() ?: false) {
            view.showFileName(selectedFile!!.path)
            view.showFileContent(selectedFile!!.toURI().toURL().toExternalForm())
            view.enableAalyzeBtn(true)
        } else {
            view.enableAalyzeBtn(false)
        }
    }

    /**
     * Perform check of the selected file
     */
    fun onAnalyzeBtnClick() {
        val file = selectedFile
        if (file != null) {
            performCheck(file)
        }
    }
}

