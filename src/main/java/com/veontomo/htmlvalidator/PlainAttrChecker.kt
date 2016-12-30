package com.veontomo.htmlvalidator

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Detect the presence of non-allowed attributes among element attributes.
 *
 * Example: the following element
 * <span title="this is a span element" class="wide" style="color: red;">...</span>
 * has attributes: "title", "class", "style".
 * @param attrPlain list of allowed attributes. Any other attribute is considered non-allowed.
 *
 */
class PlainAttrChecker(val attrPlain: Set<String>) : Checker() {
    override val descriptor = "Plain attribute checker"

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val elements = doc.select("*")
        return elements.map { it -> controlPlainAttrs(it) }.filterNot { it.isEmpty() }
                .map { it -> CheckMessage("Invalid attributes: ${it.joinToString { it }}") }
    }

    /**
     * Return non-allowed attributes found among plain ones.
     *
     * @param el element whose attributes are to be inspected.
     * @return a list of non-allowed attributes found among the element's plain attributes.
     */
    fun controlPlainAttrs(el: Element): List<String> {
        return el.attributes().filterNot { it -> attrPlain.contains(it.key.toLowerCase()) }.map { it.key }
    }

}