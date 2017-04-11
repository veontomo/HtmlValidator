package com.veontomo.htmlvalidator.Models

import com.veontomo.htmlvalidator.Models.Checkers.*
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.io.File

/**
 * Model of the MVP pattern
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
    val checkers = setOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(),
            PlainAttrChecker(attrPlain), InlineAttrChecker(attrInline), EncodingChecker(charsets), EscapeClosureChecker())

    private val subject: PublishSubject<File> = PublishSubject.create<File>()

    /**
     * Create a list of observable, one for each checker
     */
    val analyzer: List<Observable<Report>> = checkers.map {
        checker ->
        subject
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map {
                    file ->
                    generateReport(file, checker)
                }
    }

    /**
     * Perform a check of  the text with given checker.
     * @param file content of this file is to be analyzed
     * @param checker
     * @return a report
     */
    private fun generateReport(file: File, checker: Checker): Report {
        return createReport(checker.descriptor, checker.check(file.readText()))
    }


    /**
     * Reference to the most recent analyzed file. It may not exist.
     */
    private var lastAnalyzedFile: File? = null

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

    fun analyze(file: File) {
        lastAnalyzedFile = file
        subject.onNext(file)
    }


    /**
     * Check a last used file again.
     */
    fun recheck() {
        val file = lastAnalyzedFile
        if (file != null && file.exists()) {
            subject.onNext(file)
        }
    }


}