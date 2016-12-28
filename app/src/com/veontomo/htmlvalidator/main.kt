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

    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(),
            PlainAttrChecker(attrPlain), InlineAttrChecker(attrInline))
    if (args.isNotEmpty()) {
        val folder = args[0]
        println("Processing files from folder $folder")
        val directory = File(folder)
        val fList = directory.listFiles()
        fList.forEach { it -> runCheckers(it, checkers) }
    } else {
        println("No folder has been given hence no file has been processed.")
    }
}

fun runCheckers(file: File, checkers: List<Checker>) {
    val text = file.readText()
    println("File ${file.name}")
    checkers.map { checker -> checker.check(text) }.flatten().map { it -> println(it.message) }


}


