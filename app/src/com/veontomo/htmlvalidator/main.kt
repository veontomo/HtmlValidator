package com.veontomo.htmlvalidator

import java.io.File


/**
 * A script that makes the checkers run against given files.
 */
fun main(args: Array<String>) {
    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker(), WidthChecker())
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
    checkers.map { checker -> checker.check(text) }.flatten().forEach { it -> println(it.message) }

}


