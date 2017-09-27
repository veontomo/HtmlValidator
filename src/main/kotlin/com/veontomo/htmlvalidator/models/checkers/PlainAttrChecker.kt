package com.veontomo.htmlvalidator.models.checkers

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
class PlainAttrChecker(private val attrPlain: Set<String>) : Checker() {
    override val descriptor = "Plain attribute checker"

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val elements = doc.select("*")
        return elements
                .map { it -> controlPlainAttrs(it) }
                .filterNot { it.isEmpty() }
                .distinct()
                .map { it -> CheckMessage(it.joinToString("\n", "", "", -1, "",  { it }), false) }
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