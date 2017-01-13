package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Models.EncodingChecker
import org.junit.Test

import org.junit.Assert.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


/**
 *  Test suite for checking the encoding.
 */
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
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.getCharset("")).thenReturn(setOf<String>())
        `when`(mock.encodings).thenReturn(setOf<String>())
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 0
    @Test
    fun checkOneAllowedNoDetected() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.getCharset("a text")).thenReturn(setOf<String>())
        `when`(mock.encodings).thenReturn(setOf("ascii"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets:  > 1
    // 2. # detected charsets: 0
    @Test
    fun checkThreeAllowedNoDetected() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("ascii", "utf-8", "cp-1251"))
        `when`(mock.getCharset("a text")).thenReturn(setOf<String>())
        `when`(mock.check("a text")).thenCallRealMethod()
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 0
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkZeroAllowedOneDetected() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf<String>())
        `when`(mock.getCharset("a text")).thenReturn(setOf("enc1"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkOneAllowedOneDetected() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc2"))
        `when`(mock.getCharset("a text")).thenReturn(setOf("enc1"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("a text").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkOneAllowedOneDetectedCoincide() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc"))
        `when`(mock.getCharset("a string")).thenReturn(setOf("enc"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertTrue(mock.check("a string").isEmpty())
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkThreeAllowedOneDetectedCoincide() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc1", "enc2", "enc3"))
        `when`(mock.getCharset("some html string")).thenReturn(setOf("enc2"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertTrue(mock.check("some html string").isEmpty())
    }


    // Cover
    // 1. # allowed charsets: 0
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkNoAllowedTwoDetected() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.getCharset("document 1")).thenReturn(setOf("enc-1", "enc-2"))
        `when`(mock.encodings).thenReturn(setOf<String>())
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: none
    @Test
    fun checkOneAllowedTwoDetectedNoOverlap() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("encoding"))
        `when`(mock.getCharset("document 1")).thenReturn(setOf("enc-1", "enc-2"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: partially
    @Test
    fun checkOneAllowedTwoDetectedPartialOverlap() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc-1"))
        `when`(mock.getCharset("document 1")).thenReturn(setOf("enc-1", "enc-2"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.encodings.size)
        assertEquals(2, mock.getCharset("document 1").size)
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: partially
    @Test
    fun checkTwoAllowedTwoDetectedPartialOverlap() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc-1", "char"))
        `when`(mock.getCharset("document 1")).thenReturn(setOf("enc-1", "enc-2"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("document 1").size)
    }

    // Cover
    // 1. # allowed charsets: > 1
    // 2. # detected charsets: > 1
    // 3. detected charsets belong to the allowed ones: all
    @Test
    fun checkTwoAllowedTwoDetectedCompleteOverlap() {
        val mock = mock(EncodingChecker::class.java)
        `when`(mock.encodings).thenReturn(setOf("enc-2", "enc-1"))
        `when`(mock.getCharset("document 1")).thenReturn(setOf("enc-1", "enc-2"))
        `when`(mock.check(anyString())).thenCallRealMethod()
        assertEquals(1, mock.check("document 1").size)
    }

}