package com.veontomo.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import java.io.File
import java.nio.charset.Charset


/**
 * Created by Andrey on 07/06/2016.
 */
fun main(args: Array<String>) {

//    val checkers = listOf(SafeCharChecker(), AttributeSafeCharChecker())
//    val directory = File(args[0])
//    val fList = directory.listFiles()

    val charset = Charset.forName("ascii")
    val doc: Document = Jsoup.parse(File("data\\test.html").readText(charset))
    doc.outputSettings().charset(charset)
    println(doc.toString())
    doc.body().select("div").forEach { div -> println("title = ${div.attr("title")}") }
    doc.body().select("div").forEach { div ->
        val title = Jsoup.clean("${div.attr("title")}", "", Whitelist.none(), Document.OutputSettings().charset(charset))
        println("title = $title")
    }

//    fList.forEach { it -> runCheckers(it, checkers) }

}

fun runCheckers(file: File, checkers: List<Checker>) {
    checkers.map { checker -> checker.check(file) }.flatten().forEach { it -> println(it.message) }

}


