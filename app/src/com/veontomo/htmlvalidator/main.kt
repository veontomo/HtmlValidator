package com.veontomo.app

import org.jsoup.Jsoup
import java.io.File


/**
 * Created by Andrey on 07/06/2016.
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

