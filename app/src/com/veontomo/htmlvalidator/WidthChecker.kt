package com.veontomo.htmlvalidator

import org.jsoup.nodes.Element

/**
 * Check whether all elements of the document have consistent widths.
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
    override fun check(html: String): List<CheckMessage> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        val width = el.attr("width")
        // initialize by some illegal value (negative one)
        var widthInt = -1
        try {
            widthInt = Integer.valueOf(width)
        } catch (e: NumberFormatException) {
            return false
        }
        val widths = selectFromStyle(el, listOf("width", "min-width", "max-width"))
        if ((width == null || width.isBlank()) && widths.isEmpty()) {
            return true
        }
        if (widths.size != 3 || width.isNotBlank() ) {
            return false
        }
        val widthValue = widths["width"]!!
        // all widths must have the same value (i.e. equal to that of "width")
        if (!widths.all { it -> it.key.equals(widthValue) }) {
            return false
        }
        val pattern = Regex("^\\d+?px")
        val width2 = pattern.matchEntire(widthValue)?.groups?.get(1)?.value
        return width2 != null && width2.isNotBlank() && width2.equals(widthInt.toString())
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