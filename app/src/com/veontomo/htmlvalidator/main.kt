package com.veontomo.htmlvalidator

import java.io.File


/**
 * A script that makes the checkers run against given files.
 */
fun main(args: Array<String>) {
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
    val charsets = listOf("ascii")

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
                println("$name: ${if (report.isEmpty()) "OK" else "${report.size} message(s)"}")
                println(report.joinToString { it.message })
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
    return checkers.associateBy({ it.descr }, { it.check(text) })
}


