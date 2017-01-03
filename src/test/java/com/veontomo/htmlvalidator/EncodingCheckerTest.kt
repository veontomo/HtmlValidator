package com.veontomo.htmlvalidator

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.willReturn
import de.jodamob.kotlin.testrunner.KotlinTestRunner
import de.jodamob.kotlin.testrunner.OpenedClasses
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

//import org.mockito.Mockito.`when`

/**
 *  Test suite for checking the encoding.
 */
//@RunWith(KotlinTestRunner::class)
//@OpenedClasses(EncodingChecker::class)
class EncodingCheckerTest {
    /**
     * Test for the method that picks up the charset.
     * Partition strategy:
     * 1. # meta tags: 0, 1, > 1
     * 2. location of the charset: none, in "charset", in "content", in both
     */

    // Cover
    // 1. # meta tags: 0
    // 2. location of charset: none
    @Test
    fun checkZeroMetaTags() {
        val html =
                "<!DOCTYPE html><html><head><title>Title</title></head><body></body><html>"
        assertTrue(EncodingChecker(setOf()).getCharset(html).isEmpty())
    }

    // Cover
    // 1. # meta tags: 1
    // 2. location of charset: none
    @Test
    fun checkOneMetaTagsNoCharset() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html;\">" +
                        "<title>Title</title></head><body></body><html>"
        assertTrue(EncodingChecker(setOf()).getCharset(html).isEmpty())
    }

    // Cover
    // 1. # meta tags: > 1
    // 2. location of charset: none
    @Test
    fun checkTwoMetaTags() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html;\">" +
                        "<meta name=\"description\" content=\"another meta\">" +
                        "<title>Title</title></head><body></body><html>"
        assertTrue(EncodingChecker(setOf()).getCharset(html).isEmpty())
    }

    // Cover
    // 1. # meta tags: 1
    // 2. location of charset: in "charset"
    @Test
    fun checkOneMetaTagsCharset() {
        val html =
                "<!DOCTYPE html><html><head><meta charset=\"ascii\">" +
                        "<title>Title</title></head><body></body><html>"
        val charsets = EncodingChecker(setOf()).getCharset(html)
        assertEquals(1, charsets.size)
        assertTrue(charsets.contains("ascii"))
    }

    // Cover
    // 1. # meta tags: 1
    // 2. location of charset: in "content"
    @Test
    fun checkOneMetaTagsContent() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">" +
                        "<title>Title</title></head><body></body><html>"
        val charsets = EncodingChecker(setOf()).getCharset(html)
        assertEquals(1, charsets.size)
        assertTrue(charsets.contains("utf-8"))
    }

    // Cover
    // 1. # meta tags: > 1
    // 2. location of charset: in both
    @Test
    fun checkTwoMetaTagsInBoth() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">" +
                        "<meta charset=\"ascii\">" +
                        "<title>Title</title></head><body></body><html>"
        val charsets = EncodingChecker(setOf()).getCharset(html)
        assertEquals(2, charsets.size)
        assertTrue(charsets.containsAll(listOf("utf-8", "ascii")))
    }

    // Cover
    // 1. # meta tags: > 1
    // 2. location of charset: in content
    @Test
    fun checkTwoMetaTagsInContent() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html;charset=ascii\">" +
                        "<meta name=\"description\" content=\"another meta\">" +
                        "<title>Title</title></head><body></body><html>"
        val charsets = EncodingChecker(setOf()).getCharset(html)
        assertEquals(1, charsets.size)
        assertTrue(charsets.contains("ascii"))
    }

    /**
     * Test the check method that depends on getCharsets() which is to be mocked.
     * Partition the input as follows:
     * 1. # allowed charsets: 0, 1, > 1
     * 2. # detected charsets: 0, 1, > 1
     * 3. detected charsets belong to the allowed ones: none, all, partially
     */
    // Cover
    // 1. # allowed charsets: 0
    // 2. # detected charsets: 0
    @Test
    fun checkAllEmpty() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf<String>()
            on { getCharset("") } doReturn setOf<String>()
        }
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 0
    @Test
    fun checkOneAllowedNoDetected() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("ascii")
            on { getCharset("a text") } doReturn setOf<String>()
        }
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets:  > 1
    // 2. # detected charsets: 0
    @Test
    fun checkThreeAllowedNoDetected() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("ascii", "utf-8", "cp-1251")
            on { getCharset("a text") } doReturn setOf<String>()
        }
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 0
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkZeroAllowedOneDetected() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf<String>()
            on { getCharset("a text") } doReturn setOf("enc1")
        }
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkOneAllowedOneDetected() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc2")
            on { getCharset("a text") } doReturn setOf("enc1")
        }
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkOneAllowedOneDetectedCoincide() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc")
            on { getCharset("a text") } doReturn setOf("enc")
        }
        assertTrue(mock.check("a text").isEmpty())
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkThreeAllowedOneDetectedCoincide() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc1", "enc2", "enc3")
            on { getCharset("some html string") } doReturn setOf("enc2")
        }
        assertTrue(mock.check("some html string").isEmpty())
    }

    // Cover
    // 1. # allowed charsets: 0
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkNoAllowedTwoDetected() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf<String>()
            on { getCharset("document 1") } doReturn setOf("enc-1", "enc-2")
        }
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkOneAllowedTwoDetectedNoOverlap() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("encoding")
            on { getCharset("document 1") } doReturn setOf("enc-1", "enc-2")
        }
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: partially
    @Test
    fun checkOneAllowedTwoDetectedPartialOverlap() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc-1")
            on { getCharset("document 1") } doReturn setOf("enc-1", "enc-2")
        }
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: partially
    @Test
    fun checkTwoAllowedTwoDetectedPartialOverlap() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc-1", "char")
            on { getCharset("document 1") } doReturn setOf("enc-1", "enc-2")
        }
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkTwoAllowedTwoDetectedCompleteOverlap() {
        val mock = mock<EncodingChecker> {
            on { getAllowedEncodings() } doReturn setOf("enc-2", "enc-1")
            on { getCharset("document 1") } doReturn setOf("enc-1", "enc-2")
        }
        assertEquals(1, mock.check("document 1").size)
    }
}