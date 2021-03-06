package com.veontomo.htmlvalidator.models.checkers

import org.jsoup.Jsoup

/**
 * Check the encoding.
 * An html document might have the charset imposed in the tag "meta" in one of the two ways:

 * <meta charset="ascii">
 * <meta http-equiv="content-type" content="text/html; charset=utf-8">

 * @param allowedEncodings list of allowed encodings
 */
class EncodingChecker(private val allowedEncodings: Set<String>) : Checker() {
    override val descriptor = "Encoding checker"

    // create a defencive copy in order to avoid the rep exposure
    val encodings: Set<String> get() {
        return allowedEncodings.map { it }.toSet()
    }

    override fun check(html: String): List<CheckMessage> {
        val charsets = getCharset(html)
        val message = when (charsets.size) {
            0 -> "No charset is found."
            1 -> {
                val charset = charsets.first()
                if (encodings.contains(charset)) {
                    null
                } else {
                    "The charset \"$charset\" is not among allowed ones: \"${encodings.joinToString { it }}\"."
                }
            }
            else -> "Multiple charsets are found: ${charsets.joinToString { it }}"
        }
        return if (message == null) listOf() else listOf(CheckMessage(message, false))
    }


    /**
     * Return all the charsets as it is specified in the meta tag inside the attribute "content" or "charset".
     * If no charset is found, empty set is returned.
     * @param html string representation of the html document
     * @return list of all found charsets. Might be empty.
     */
    fun getCharset(html: String): Set<String> {
        val token1 = "charset"
        val token2 = "content"
        val result = mutableSetOf<String>()
        val doc = Jsoup.parse(html)
        val metaTags = doc.select("meta")
        for (meta in metaTags) {
            if (meta.hasAttr(token1)) {
                result.add(meta.attr(token1))
            }
            if (meta.hasAttr(token2)) {
                val charsets = meta.attr(token2).split(";")
                        .filter { it.matches(Regex("\\s?$token1=.*$")) }
                        .map { it.replace("$token1=", "").trim() }
                result.addAll(charsets)
            }
        }
        return result
    }
}