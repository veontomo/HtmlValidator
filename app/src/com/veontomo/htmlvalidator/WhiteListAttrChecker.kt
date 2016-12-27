package com.veontomo.htmlvalidator

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Detect presence of attributes that are not in "white list".
 *
 * There are two types of attributes:
 * 1. attributes of the form name="value", i.e.: attribute "href" in this tag:
 * <a href="www.google.com"  ...>...</a>
 * 2. attributes of the "style" one:
 * <a style="color: blue;text-decoration: underline;">...</a>
 *
 * Every element is allowed to have attributes of the first type from the set "attrPlain" and
 * the attributes of the second type from the set "attrInline".
 *
 */
class WhiteListAttrChecker(val attrPlain: Set<String>, val attrInline: Set<String>) : Checker() {
//    private val attrPlain = setOf("title", "href", "width", "height", "alt", "src", "style")
//    private val attrInline = setOf(
//            "width", "max-width", "min-width",
//            "padding", "padding-top", "padding-bottom", "padding-left", "padding-right",
//            "margin", "margin-top", "margin-bottom", "margin-left", "margin-right",
//            "text-decoration", "text-align", "line-height",
//            "font-size", "font-weight", "font-family", "font-style",
//            "border", "border-style", "border-spacing",
//            "color",
//            "display"
//    )

    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val elements = doc.select("*")
        return elements.filterNot { it -> hasPlainAttrsFrom(it, attrPlain) && hasInlineAttrsFrom(it, attrInline) }
                .map { it -> CheckMessage("Invalid attributes in ${it.tagName()}: ${it.attributes().joinToString { it.key + "=\"" + it.value + "\"" }}" ) }
    }

    /**
     * Check whether the element contains only allowed attributes.
     *
     * @param el element whose attributes are to be inspected.
     * @param attrs set of allowed attributes
     * @return true if all element's attribute are in the set "attrs", false otherwise.
     */
    fun hasPlainAttrsFrom(el: Element, attrs: Set<String>): Boolean {
        return el.attributes().all { it -> attrs.contains(it.key) }
    }

    /**
     * Check whether the "style" attribute of the element contains only the keys from the
     * given set.
     * @param el element whose style attribute is to be inspected
     * @param attrs set of allowed keys in the style attribute
     * @return true if all attributes inside the "style" are allowed ones, false otherwise
     */
    fun hasInlineAttrsFrom(el: Element, attrs: Set<String>): Boolean {
        return el.attr("style")
                .split(";")
                .map { it -> it.split(":") }
                .filter { it.size == 2 }
                .map { it[0].trim() }
                .filterNot { attrs.contains(it) }
                .isEmpty()
    }
}