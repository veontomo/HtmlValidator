package com.veontomo.htmlvalidator

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultRedirectStrategy
import org.apache.http.client.config.CookieSpecs
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HttpContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Entities
import java.net.URI
import java.nio.charset.Charset

/**
 * Create report for every link found in the html document.
 * The report refers to a validity of a link: the number of intermediate url used to retrieve
 * the requested one (redirects), the number of bytes received.
 */
class LinkChecker : Checker() {
    override val descriptor: String = "Link checker"

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        doc.charset(Charset.forName("ASCII"))
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml)
        val body = doc.body()
        val links = body.select("a")
        return links.map { link -> link.attr("href") }.distinct().map { it -> analyze(it) }
    }

    /**
     * Return a report concerning given url.
     * @param url
     * @return  a message about the number of bytes in the received response and the number of redirects
     */
    private fun analyze(url: String): CheckMessage {
        val strategy = RedirectLogger()
        val client = HttpClients.custom()
                .setRedirectStrategy(strategy)
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .build()
        val msgBuilder = StringBuilder()
        var status = true
        try {
            val response = client.execute(HttpGet(url), BasicResponseHandler())
            msgBuilder.append("$url returned ${response.length} bytes")

        } catch (e: Exception) {
            msgBuilder.append("Error while connecting to url \"$url\": ${e.message}")
            status = false
        }
        val history = strategy.log
        if (history.isNotEmpty()) {
            msgBuilder.append(" after ${history.size} redirects:\n ${history.joinToString(",\n ", "", "", 5, "...", null)}")
        }
        return CheckMessage(msgBuilder.toString(), status)
    }


    /**
     * Immutable class for tracking the log of redirects that occur during retrieval of an uri.
     * The immutability is achieved by using an immutable type (String) and defencive copy.
     */
    private class RedirectLogger() : DefaultRedirectStrategy() {
        private val redirectSequence = mutableListOf<URI>()

        val log: List<String>
            get() = redirectSequence.map(URI::toString)

        override fun getRedirect(request: HttpRequest?, response: HttpResponse?, context: HttpContext?): HttpUriRequest {
            val redirect: HttpUriRequest = super.getRedirect(request, response, context)
            redirectSequence.add(redirect.uri)
            return redirect
        }


    }
}
