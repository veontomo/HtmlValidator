package com.veontomo.htmlvalidator

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

}