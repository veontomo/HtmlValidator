package com.veontomo.app

/**
 * Find a break-line tag <br> (or <br />) inside attribute values of other html tags.
 * For example, find  illegal <br> tag in the following string:
 * <img style="width: 100px;" alt="alt<br>text" src="img.jpg">
 */
class NoNestedBrChecker : Checker(){
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param txt string whose validity is to be checked. Not modified by this method.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(txt: String): List<CheckMessage> {
        throw UnsupportedOperationException("not implemented")
    }
}