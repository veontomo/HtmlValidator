package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.models.checkers.SafeCharChecker
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Test the class that detects presence of  non-ascii characters.
 */
class SafeCharCheckerTest {

    private var checker: SafeCharChecker? = null

    @Before
    fun setUp() {
        checker = SafeCharChecker()
    }

    @After
    fun tearDown() {
        checker = null
    }

    /**
     * Test method a list of messages concerning non-safe chars found in given text
     * Partition the input as follows:
     * 1. the input string length: 0, > 0
     * 2. non-safe char positions: none, the same line, different lines
     * 3. # newline symbol: 0, 1, > 1
     * 4. line ending format: DOS (\r\n), Unix (\n)
     */
    // Cover
    // 1. input string length: 0
    // 2. non-safe char positions: none
    // 3. # newline symbols: 0
    @Test
    fun checkZeroInputLength() {
        assertTrue("must return empty list", checker!!.check("").isEmpty())
    }

    // Cover
    // 1. input string length: > 0
    // 2. non-safe char positions: none
    // 3. # newline symbols: > 1
    // 4. line ending format: DOS
    @Test
    fun checkThreeLinesAllSafeDOS() {
        assertTrue("must return empty list", checker!!.check("hi\r\nhow are you?\r\nBye").isEmpty())
    }

    // Cover
    // 1. input string length: > 0
    // 2. non-safe char positions: 0
    // 3. # newline symbols: > 1
    // 4. line ending format: Unix
    @Test
    fun checkThreeLinesAllSafeUnix() {
        assertTrue("must return empty list", checker!!.check("hi\nhow are you?\nBye\nPS: see you later").isEmpty())
    }

    // Cover
    // 1. input string length: > 0
    // 2. non-ascii char positions: same line
    // 3. # newline symbols: 0
    @Test
    fun checkSingleNonAsciiSymbol() {
        val result = checker!!.check("à")
        assertTrue(result.isNotEmpty())
    }

    // Cover
    // 1. input string length: > 0
    // 2. non-ascii char positions: different lines
    // 3. # newline symbols: > 1
    // 4. line ending format: DOS
    @Test
    fun checkMultilineDOS() {
        val result = checker!!.check("Hé \r\nthere à là")
        assertTrue(result.isNotEmpty())
    }

    // Cover
    // 1. input string length: > 0
    // 2. non-ascii char positions: different lines
    // 3. # newline symbols: > 1
    // 4. line ending format: Unix
    @Test
    fun checkMultilineUnix() {
        val result = checker!!.check("Hé \nthere\n à\nlà")
        assertTrue(result.isNotEmpty())
    }

    /**
     * Test the method that check whether the given char is safe or not.
     * The strategy is to partition the input as follows:
     * ascii codes [0, 9] ->  non-safe
     * \r (ascii code 10) -> safe
     * ascii codes [11, 12] -> non-safe
     * \n (ascii code 13) -> safe
     * ascii codes [14, 31] -> non-safe
     * ascii codes [32, 126] -> safe
     * ascii codes [127, 255] -> non-safe
     */
    // Cover range [0, 9]
    @Test
    fun isSafeChar0To9() {
        (0..9).forEach { i ->
            val c = i.toChar()
            assertFalse("$c must be non-safe ", checker!!.isSafeChar(c))
        }
    }

    // Cover \n
    @Test
    fun isSafeCharLineFeed() {
        assertTrue("line feed char must be safe", checker!!.isSafeChar('\n'))
    }

    // Cover range [11, 12]
    @Test
    fun isSafeChar11To12() {
        (11..12).forEach { i ->
            val c = i.toChar()
            assertFalse("$c must be non-safe", checker!!.isSafeChar(c))
        }
    }

    // Cover \r
    @Test
    fun isSafeCharCarriageReturn() {
        assertTrue("carriage return must be safe", checker!!.isSafeChar('\r'))
    }

    // Cover range [14, 31]
    @Test
    fun isSafeChar14To31() {
        (14..31).forEach { i ->
            val c = i.toChar()
            assertFalse("$c must be non-safe", checker!!.isSafeChar(c))
        }
    }

    // Cover range [32, 126]
    @Test
    fun isSafeChar32To126() {
        (32..126).forEach { i ->
            val c = i.toChar()
            assertTrue("$c must be safe", checker!!.isSafeChar(c))
        }
    }

    // Cover range [127, 255]
    @Test
    fun isSafeChar127To255() {
        (127..255).forEach { i ->
            val c = i.toChar()
            assertFalse("$c must be non-safe", checker!!.isSafeChar(c))
        }
    }
}

