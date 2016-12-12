package com.veontomo.htmlvalidator

import java.io.File


/**
 * A script that makes the checkers run against given files.
 */
fun main(args: Array<String>) {
    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker(), LinkChecker())
    val directory = File(args[0])
    val fList = directory.listFiles()
    fList.forEach { it -> runCheckers(it, checkers) }
}

fun runCheckers(file: File, checkers: List<Checker>) {
    val text = file.readText()
    checkers.map { checker -> checker.check(text) }.flatten().forEach { it -> println(it.message)}

}


