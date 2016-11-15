package com.veontomo.app


/**
 * Find non-safe characters in given string.
 * A character is a safe one iff its ascii code is one of these numbers: 10, 32, 33, 34, ..., 126.
 * Otherwise, the character is considered non-safe.
 *
 * Set of safe ascii codes consists of the following integers:
 * 10 (corresponds to line feed),
 * 13 (corresponds to carriage return)
 * [32, 126].
 */
class SafeCharChecker : Checker() {

    private val LINEBREAK = System.getProperty("line.separator")

    /**
     * Stand apart ascii code (line feed)
     */
    private val LINEFEED = 10

    /**
     * Stand apart ascii code (carriage return)
     */
    private val CARRIAGERETURN = 13

    /**
     * Left boundary for the range
     */
    private val LEFT_BOUNDARY = 32
    /**
     * Right boundary for the range
     */
    private val RIGHT_BOUNDARY = 126

    /**
     * Check the validity of given string and return a list of messages related to found non-safe chars.
     * One message might refer to multiple non-safe chars. For example, if some line of the given text contains
     * multiple non-safe chars, their might be a single message that corresponds to those non-safe characters.
     * @param html string whose validity is to be checked. Not modified by this method.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(html: String): List<CheckMessage> {
        val messages = mutableListOf<CheckMessage>()
        val lines = html.split(LINEBREAK)
        lines.forEachIndexed { i, s ->
            val res = filterOutSafe(s)
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
    fun filterOutSafe(text: String): List<Char> {
        return text.toCharArray().filterNot { c -> isSafeChar(c) }
    }

    /**
     * Whether a character is safe or not.
     * Definition of a safe character is given in the class description.
     */
    fun isSafeChar(c: Char): Boolean {
        val code = c.toInt()
        return code == LINEFEED || code == CARRIAGERETURN || (code >= LEFT_BOUNDARY && code <= RIGHT_BOUNDARY)
    }

}