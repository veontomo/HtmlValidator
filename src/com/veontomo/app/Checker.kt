package com.veontomo.app

import java.io.File

/**
 * Superclass for checking validity of given string.
 */
abstract class Checker {
    /**
     * Check the validity of given file and return a list of messages related to found irregularities.
     * @param file file whose validity is to be checked. Not modified by this method.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    abstract fun check(file: File): List<CheckMessage>
}

