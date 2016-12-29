package com.veontomo.htmlvalidator

import org.jsoup.Jsoup

/**
 * Check the encoding.
 * An html document encoding should be set in tag "meta" as follows:
 *
 * <meta http-equiv="content-type" content="text/html; charset=utf-8">
 * @param encoding list of allowed encodings
 */
class EncodingChecker(val encodings: List<String>): Checker() {
    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val metas = doc.select("meta")
    }

    override val descr  = "Encoding checker"

}