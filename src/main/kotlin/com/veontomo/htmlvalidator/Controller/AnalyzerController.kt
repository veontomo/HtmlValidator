package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.*
import com.veontomo.htmlvalidator.Models.Report
import com.veontomo.htmlvalidator.Views.MainView
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.io.File

/**
 * A controller that orchestrates execution of available checks of given file.
 */
class AnalyzerController {


    @FXML
    val checkersView = TableView<Report>()
    @FXML
    val fileNameText = Text()
    @FXML
    val fileInfoText = Text()
    @FXML
    val browser = WebView()

    val attrPlain = setOf("title", "href", "width", "height", "alt", "src", "style", "target",
            "http-equiv", "content", "cellpadding", "cellspacing", "lang", "border")
    val attrInline = setOf(
            "width", "max-width", "min-width",
            "padding", "margin",
            "text-decoration", "text-align", "line-height",
            "font-size", "font-weight", "font-family", "font-style",
            "border", "border-style", "border-spacing", "border-collapse",
            "border-top", "border-bottom",
            "color", "height",
            "display", "vertical-align", "background-color"
    )
    val charsets = setOf("ascii")

    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(),
            PlainAttrChecker(attrPlain), InlineAttrChecker(attrInline), EncodingChecker(charsets))

    var selectedFile: File? = null

    init {
//        view.enableSelect(true)
//        view.enableAnalyze(false)
//        view.enableClear(false)
//        view.loadItems(createEmptyReport())
    }

    private fun performCheck(file: File): List<Report> {
        val text = file.readText()
        return checkers.map { createReport(it.descriptor, it.check(text)) }
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
    fun onAnalyze() {
        val file = selectedFile
        if (file != null) {
//            view.enableAnalyze(false)
//            view.enableSelect(false)
//            val reports = performCheck(file)
//            view.loadItems(reports)
//            view.enableAnalyze(true)
//            view.enableSelect(true)
        }
    }

    fun setFile(file: File) {
        selectedFile = file
    }

    /**
     * Clear the file
     */
    fun onClear() {
        selectedFile = null
//        view.enableClear(false)
//        view.enableAnalyze(false)
//        view.showFileInfo("")
//        view.showFileName("")
//        view.showFileContent(null)
//        view.loadItems(createEmptyReport())
    }

    /**
     * Create a fictitious report in order to clear the table with checkers' results.
     */
    private fun createEmptyReport(): List<Report> {
        return checkers.map { Report(it.descriptor, null, null) }
    }
}

