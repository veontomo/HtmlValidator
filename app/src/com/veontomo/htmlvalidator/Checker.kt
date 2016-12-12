package com.veontomo.htmlvalidator

/**
 * Superclass for checking validity of given string.
 */
abstract class Checker {
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param html string whose validity is to be checked. Not modified by this method.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    abstract fun check(html: String): List<CheckMessage>
}

