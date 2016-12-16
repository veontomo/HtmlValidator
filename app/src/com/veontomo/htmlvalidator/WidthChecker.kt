package com.veontomo.htmlvalidator

import org.jsoup.nodes.Element
import java.util.*

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

    private val xxx = "aaa"
    /**
     * Control the consistency of width parameters of a given element.
     * The element consistency is defined in the class description.
     *
     * @param el element whose width is to be inspected
     * @return true if the element's width is consistent, false otherwise
     *
     */
    fun isConsistent(el: Element): Boolean {
        /// stub
        return false
    }

    /**
     * Pick up the given attributes from the element's style attribute.
     * @param el element which style attribute is to be inspected
     * @param attrs list of attributes whose values are to selected from the element's style attribute
     * @return a map from requested attributes to their values. If an attribute is not present in the style attribute,
     * then the returned value of that attribute is null.
     */
    fun selectFromStyle(el: Element, attrs: List<String>): Map<String, String> {
        /// stub
        return mapOf<String, String>()
    }

}