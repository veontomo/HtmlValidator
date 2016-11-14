package com.veontomo.app


/**
 * Find non-ascii characters in given string.
 * A character is an ascii one iff its ascii code is equal to one of these decimal numbers: 10, 32, 33, 34, ..., 126.
 * Set of allowed ascii codes consists of two disjoint set of integers: one contains just one value 10 that corresponds
 * to a space " ", the other contains integers from 32 (inclusive) to 126 (inclusive).
 */
class AsciiChecker : Checker() {

    private val LINEBREAK = System.getProperty("line.separator")

    /**
     * Stand apart ascii code (space character)
     */
    private val SPACE = 10
    /**
     * Left boundary for ascii code
     */
    private val LEFT_BOUNDARY = 32
    /**
     * Right boundary for ascii code
     */
    private val RIGHT_BOUNDARY = 126
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param txt string whose validity is to be checked. Not modified by this method.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(txt: String): List<CheckMessage> {
        val messages = mutableListOf<CheckMessage>()
        val lines = txt.split(LINEBREAK)
        lines.forEachIndexed { i, s ->
            val res = filterOutValid(s)
            if (!res.isEmpty()) {
                messages.add(CheckMessage("line ${i + 1} \"$s\" contains non-ascii symbols: ${res.joinToString { it -> "\"$it\" (ascii code: ${it.toInt()})" }}"))
            }
        }
        return messages
    }

    /**
     * Find all non-ascii characters of given string.
     * @param text
     */
    fun filterOutValid(text: String): List<Char> {
        return text.toCharArray().filterNot { c ->
            run { val code = c.toInt(); code == SPACE || (code >= LEFT_BOUNDARY && code <= RIGHT_BOUNDARY) }
        }
    }

}