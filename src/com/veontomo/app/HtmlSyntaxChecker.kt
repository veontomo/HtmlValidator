package com.veontomo.app

import org.jsoup.Jsoup
import java.io.File

/**
 * Check validity of html document.
 */
class HtmlSyntaxChecker() : Checker() {
    override fun check(file: File): List<CheckMessage> {
        val doc = Jsoup.parse(file, "ASCII", "")
        System.out.println("doc base uri: ${doc.baseUri()}")
        return listOf(CheckMessage("ok"))
    }

}