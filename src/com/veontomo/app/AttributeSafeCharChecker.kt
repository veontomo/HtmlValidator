package com.veontomo.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Find a non-safe characters inside attribute values of html tags.
 *
 * Only these characters are considered safe for an html tag attribute value:
 * 0-9, a-z, A-Z, ;, :, ., comma, space, &, #, !, ?, -, +, =, /, $, %, (, )
 *
 */
class AttributeSafeCharChecker : Checker() {

    private val safeChars = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz;:.,&#!?-+=/$%()".toSet()
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param html string whose validity is to be checked. Assume it is a valid html document.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val body = doc.body()
        val elements = body.children()
        val messages = mutableListOf<CheckMessage>()
        elements
                .map { checkElementAttributes(it) }
                .filter { it.isNotEmpty() }
                .forEach { messages.addAll(it) }
        return messages
    }

    /**
     * Check given element and return messages concerning non-safe characters in the element attribute values.
     * A single message of the resulting list might refer to multiple issues found in the element attributes.
     * @param el element whose attributes are to be inspected
     * @return list of messages.
     */
    fun checkElementAttributes(el: Element): List<CheckMessage> {
        val result = mutableListOf<CheckMessage>()
        for (attr in el.attributes()) {
            val key = attr.key
            val value = attr.value
            val isSafe = value.toCharArray().all { c -> isSafeChar(c) }
            if (!isSafe) {
                result.add(CheckMessage("$key attribute value $value is not safe."))
            }
        }
        return result
    }


    /**
     * Whether the given char is safe for being inside an html tag attribute value or not.
     * The set of safe chars is defined in the description of the class.
     */
    fun isSafeChar(c: Char): Boolean = safeChars.contains(c)

}