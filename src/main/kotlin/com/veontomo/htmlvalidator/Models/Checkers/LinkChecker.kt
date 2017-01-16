package com.veontomo.htmlvalidator.Models.Checkers

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.protocol.HttpContext
import java.net.URI

/**
 * Create report for every link found in the html document.
 * The report refers to a validity of a link: the number of intermediate url used to retrieve
 * the requested one (redirects), the number of bytes received.
 */
class LinkChecker : com.veontomo.htmlvalidator.Models.Checkers.Checker() {
    override val descriptor: String = "Link checker"

    override fun check(html: String): List<com.veontomo.htmlvalidator.Models.Checkers.CheckMessage> {
        val doc = org.jsoup.Jsoup.parse(html)
        doc.charset(java.nio.charset.Charset.forName("ASCII"))
        doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)
        val body = doc.body()
        val links = body.select("a")
        return links.map { link -> link.attr("href") }.distinct().map { it -> analyze(it) }
    }

    /**
     * Return a report concerning given url.
     * @param url
     * @return  a message about the number of bytes in the received response and the number of redirects
     */
    private fun analyze(url: String): com.veontomo.htmlvalidator.Models.Checkers.CheckMessage {
        val strategy = com.veontomo.htmlvalidator.Models.Checkers.LinkChecker.RedirectLogger()
        val client = org.apache.http.impl.client.HttpClients.custom()
                .setRedirectStrategy(strategy)
                .setDefaultRequestConfig(org.apache.http.client.config.RequestConfig.custom().setCookieSpec(org.apache.http.client.config.CookieSpecs.STANDARD).build())
                .build()
        val msgBuilder = StringBuilder()
        var status = true
        try {
            val response = client.execute(org.apache.http.client.methods.HttpGet(url), org.apache.http.impl.client.BasicResponseHandler())
            msgBuilder.append("$url returned ${response.length} bytes")

        } catch (e: Exception) {
            msgBuilder.append("Error while connecting to url \"$url\": ${e.message}")
            status = false
        }
        val history = strategy.log
        if (history.isNotEmpty()) {
            msgBuilder.append(" after ${history.size} redirects:\n ${history.joinToString(",\n ", "", "", 5, "...", null)}")
        }
        return com.veontomo.htmlvalidator.Models.Checkers.CheckMessage(msgBuilder.toString(), status)
    }


    /**
     * Immutable class for tracking the log of redirects that occur during retrieval of an uri.
     * The immutability is achieved by using an immutable type (String) and defencive copy.
     */
    private class RedirectLogger() : org.apache.http.impl.client.DefaultRedirectStrategy() {
        private val redirectSequence = mutableListOf<java.net.URI>()

        val log: List<String>
            get() = redirectSequence.map(URI::toString)

        override fun getRedirect(request: HttpRequest?, response: HttpResponse?, context: HttpContext?): HttpUriRequest {
            val redirect: HttpUriRequest = super.getRedirect(request, response, context)
            redirectSequence.add(redirect.uri)
            return redirect
        }


    }
}
