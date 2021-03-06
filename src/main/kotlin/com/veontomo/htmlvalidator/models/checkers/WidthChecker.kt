package com.veontomo.htmlvalidator.models.checkers

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Check whether all elements of the document have consistent widths.
 *
 * This checker is to be revisited because not all elements should have "min-width", "max-width"
 * attributes, i.e.: <img width="10" style="width:10px;" ... />
 *
 * The width of an element can be set by parameters "width", "max-width" and "min-width".
 *
 * In order to an element to have its width set up consistently, all of the following should hold:
 * 1. the element has attribute "width" with non-negative integer value with no unit of measurements
 * 2. the element has attribute "style" that has keys "width", "min-width" and "max-width". Their
 *    values should be equal and unit of measurement should be "px".
 * 3. the numerical values of the above-mentioned parameters should be equal.
 *
 * Example of elements with consistent width attributes:
 * <span>...</span>
 * <span width="100" style="width:100px; min-width: 100px; max-width: 100px;">...</span>
 * <div width="10" style="color: red;width:10px; min-width: 100px; max-width: 100px;">...</div>
 *
 * Example of elements with inconsistent width attributes:
 * <span width="100" style="width:100px; min-width: 50px; max-width: 100px;">...</span>
 * <span width="100.5" style="width:100px; min-width: 100px; max-width: 100px;">...</span>
 * <span width="100em" style="width:100px; min-width: 100px; max-width: 100px;">...</span>
 * <span width="100" style="width:100em; min-width: 100em; max-width: 100em;">...</span>
 *
 */
class WidthChecker : Checker() {
    override val descriptor = "Width checker"

    override fun check(html: String): List<CheckMessage> {
        val document = Jsoup.parse(html)
        val elements = document.select("*")
        val messages: MutableList<CheckMessage> = mutableListOf()
        elements.filterNot { isConsistent(it) }
                .forEach { messages.add(CheckMessage("Inconsistent width ${it.tagName()}: ${it.attributes().joinToString { it.key + "=\"${it.value}\"" }}", false)) }
        return messages

    }

    /**
     * Control the consistency of width parameters of a given element.
     * The element consistency is defined in the class description.
     *
     * @param el element whose width is to be inspected
     * @return true if the element's width is consistent, false otherwise
     *
     */
    fun isConsistent(el: Element): Boolean {
        val widthAttr = el.attr("width")
        val widthKeys = listOf("width", "min-width", "max-width")
        val widthsMap = selectFromStyle(el, widthKeys)
        // it is consistent if no widths are given
        if ((widthAttr == null || widthAttr.isBlank()) && widthsMap.isEmpty()) {
            return true
        }
        if (widthsMap.keys.size != widthKeys.size) {
            return false
        }
        // once the width attribute is given, it must contain only digits
        val patternDigits = Regex("^\\d+?$")
        if (!patternDigits.matches(widthAttr)) {
            return false
        }
        // the element style attribute must contain the widths that all have the same value
        val values = widthsMap.values.distinct()
        if (values.size != 1) {
            return false
        }
        // the width in the style attribute must be an integer followed by "px"
        val patternDigitsPx = Regex("^(\\d+?)px")
        val widthStyle = patternDigitsPx.matchEntire(values[0])?.groups?.get(1)?.value
        return widthStyle != null && widthStyle.isNotBlank() && widthStyle == widthAttr
    }

    /**
     * Pick up the given attributes from the element's style attribute.
     * In the resulting map, the trailing spaces are removed from both keys and values.
     * @param el element which style attribute is to be inspected
     * @param attrs list of attributes whose values are to selected from the element's style attribute
     * @return a map from requested attributes to their values. If an attribute is not present in the style attribute,
     * then the returned value of that attribute is null.
     */
    fun selectFromStyle(el: Element, attrs: List<String>): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val style = el.attr("style")
        val pairs = style.split(";")
        pairs.map { it -> it.split(":") }
                .filter { it -> it.size == 2 }
                .map { it -> listOf(it[0].trim(), it[1].trim()) }
                .filter { it -> attrs.contains(it[0]) }
                .forEach { it -> result.put(it[0], it[1]) }
        return result
    }

}