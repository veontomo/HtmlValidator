package com.veontomo.app

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultRedirectStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HttpContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Entities
import java.net.URI
import java.nio.charset.Charset

/**
 * Create report for every link found in the html document.
 */
class LinkChecker : Checker() {

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
        val client = HttpClients.custom().setRedirectStrategy(strategy).build()
        val msgBuilder = StringBuilder()
        try {
            val response = client.execute(HttpGet(url), BasicResponseHandler())
            msgBuilder.append("$url returned ${response.length} bytes")

        } catch (e: Exception) {
            msgBuilder.append("Error while connecting to $url: ${e.message}")
        }
        val history = strategy.history
        if (history.isNotEmpty()) {
            msgBuilder.append(" after ${history.size} redirects:  ${history.joinToString(", ", "", "", 5, "...", null)}")
        }
        return CheckMessage(msgBuilder.toString())
    }

}


/**
 * Immutable class for tracking the redirects that occurs during retrieval of an uri.
 */
private class RedirectLogger() : DefaultRedirectStrategy() {
    private val redirectSequence = mutableListOf<URI>()

    val history: List<String>
        get() = redirectSequence.map(URI::toString)

    override fun getRedirect(request: HttpRequest?, response: HttpResponse?, context: HttpContext?): HttpUriRequest {
        val redirect: HttpUriRequest = super.getRedirect(request, response, context)
        redirectSequence.add(redirect.uri)
        return redirect
    }


}
