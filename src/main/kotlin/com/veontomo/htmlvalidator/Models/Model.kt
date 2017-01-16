package com.veontomo.htmlvalidator.Models

import com.veontomo.htmlvalidator.Models.Checkers.*
import java.io.File

/**
 * Created by Andrey on 16/01/2017.
 */
class Model {
    private val STATUS_OK = "ok"

    private val STATUS_FAILURE = "Fail"
    private val STATUS_UNKNOWN = ""
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

    fun performCheck(file: File): List<Report> {
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
        val statusStr = if (status) STATUS_OK else STATUS_FAILURE
        return Report(origin, statusStr, summary)
    }

    /**
     * Create a fictitious report in order to clear the table with checkers' results.
     */
    fun createEmptyReport(): List<Report> {
        return checkers.map { Report(it.descriptor, STATUS_UNKNOWN, null) }
    }
}