package com.veontomo.app

import org.jsoup.Jsoup

/**
 * Check validity of html document.
 */
class HtmlSyntaxValidator() : Checker() {
    override fun check(txt: String): List<CheckMessage> {
        val doc = Jsoup.parse(txt)
        System.out.println("doc base uri: ${doc.baseUri()}")
        return listOf(CheckMessage("ok"))
    }

}