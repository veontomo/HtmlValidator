package com.veontomo.htmlvalidator

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Detect the presence of the non-allowed inline attributes.
 *
 * Inline attributes are the attributes that are located inside the "style" attribute, i.e.:
 * <div style="padding: 10px; font-weight: bold;">...</div>
 *
 * @param attrs a set of allowed inline attributes. Any inline attribute that is not in this set
 *              is considered non-allowed.
 */
class InlineAttrChecker(val attrs: Set<String>) : Checker() {
    override val descriptor = "Inline attribute checker"

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val elements = doc.select("*")
        return elements.map { it -> controlInlineAttrs(it) }.filterNot { it.isEmpty() }
                .map { it -> CheckMessage("Invalid inline attributes: ${it.joinToString { it }}") }
    }

    /**
     * Return non-allowed attributes found in the element's "style" attribute.
     *
     * @param el element whose style attribute is to be inspected
     * @return list of all non-allowed attributes found inside the "style"
     */
    fun controlInlineAttrs(el: Element): List<String> {
        return el.attr("style")
                .split(";")
                .map { it -> it.split(":") }
                .filter { it.size == 2 }
                .map { it[0].trim().toLowerCase() }
                .filterNot { attrs.contains(it) }
                .map { it }
    }
}