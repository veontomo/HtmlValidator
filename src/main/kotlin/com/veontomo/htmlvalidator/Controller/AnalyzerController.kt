package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.*
import com.veontomo.htmlvalidator.*
import com.veontomo.htmlvalidator.Models.Report
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

/**
 * A controller that orchestrates execution of available checks of given file.
 */
class AnalyzerController(val stage: Stage, val view: GUI) {

    val attrPlain = setOf("title", "href", "width", "height", "alt", "src", "style", "target", "http-equiv", "content", "cellpadding", "cellspacing")
    val attrInline = setOf(
            "width", "max-width", "min-width",
            "padding", "margin",
            "text-decoration", "text-align", "line-height",
            "font-size", "font-weight", "font-family", "font-style",
            "border", "border-style", "border-spacing",
            "border-top", "border-bottom",
            "color", "height",
            "display", "vertical-align", "background-color"
    )
    val charsets = setOf("ascii")

    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(),
            PlainAttrChecker(attrPlain), InlineAttrChecker(attrInline), EncodingChecker(charsets))

    var selectedFile: File? = null

    init {
        view.enableSelectBtn(true)
        view.enableAnalyzeBtn(false)
        view.loadItems(checkers.map { Report(it.descriptor, true, "") })
    }

    private fun performCheck(file: File) {
        val text = file.readText()
        view.loadItems(checkers.map { createReport(it.descriptor, it.check(text)) })
        view.enableAnalyzeBtn(true)
        view.enableSelectBtn(true)
    }

    /**
     * Merge check messages into a single report.
     * Assume that all check messages originate from the same checker.
     * The status of the resulting report instance is to be true if and only if statuses of all
     * check messages are true. If the list of messages is empty, the report status is true.
     * @param messages list of messages. Require that all messages have the same origin.
     * @return a report summarizing the check messages
     */
    private fun createReport(origin: String, messages: List<CheckMessage>): Report {
        val status = messages.isEmpty() || messages.all { it.status }
        val summary = messages.joinToString("\n", "", "", -1, "", { it.msg })
        return Report(origin, status, summary)
    }


    /**
     * Perform check of the selected file
     */
    fun onAnalyzeBtnClick() {
        val file = selectedFile
        if (file != null) {
            view.enableAnalyzeBtn(false)
            view.enableSelectBtn(false)
            performCheck(file)
        }
    }

    fun  setFile(file: File) {
        selectedFile = file
    }
}

