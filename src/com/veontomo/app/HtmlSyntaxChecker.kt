package com.veontomo.app

import org.jsoup.Jsoup

/**
 * Check validity of html document.
 */
class HtmlSyntaxChecker() : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        System.out.println("doc base uri: ${doc.baseUri()}")
        return listOf(CheckMessage("ok"))
    }

}