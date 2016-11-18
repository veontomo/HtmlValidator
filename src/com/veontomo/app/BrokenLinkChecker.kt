package com.veontomo.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Entities
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * Find broken links.
 */
class BrokenLinkChecker : Checker() {

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)

        doc.charset(Charset.forName("ASCII"))
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml)
        val body = doc.body()
        val links = body.select("a")
        return links.map { link -> link.attr("href") }.distinct().map { it -> isBroken(it) }.filterNotNull()
    }

    /**
     * return true if given resource does not exist
     */
    private fun isBroken(url: String): CheckMessage? {
        val u = URL(url)
        val huc: HttpURLConnection = u.openConnection() as HttpURLConnection
        huc.requestMethod = "GET"
        huc.connect()
        val code = huc.getResponseCode()
//        if (code != HttpURLConnection.HTTP_OK) {
        return CheckMessage("$url has returned $code ${huc.contentLength}")
//        }
//        return null
    }
}