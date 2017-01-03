package com.veontomo.htmlvalidator

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.willReturn
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.`when`

//import org.mockito.Mockito.`when`

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
        assertTrue(EncodingChecker(listOf()).getCharset(html).isEmpty())
    }

    // Cover
    // 1. # meta tags: 1
    // 2. location of charset: none
    @Test
    fun checkOneMetaTagsNoCharset() {
        val html =
                "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html;\">" +
                        "<title>Title</title></head><body></body><html>"
        assertTrue(EncodingChecker(listOf()).getCharset(html).isEmpty())
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
        assertTrue(EncodingChecker(listOf()).getCharset(html).isEmpty())
    }

    // Cover
    // 1. # meta tags: 1
    // 2. location of charset: in "charset"
    @Test
    fun checkOneMetaTagsCharset() {
        val html =
                "<!DOCTYPE html><html><head><meta charset=\"ascii\">" +
                        "<title>Title</title></head><body></body><html>"
        val charsets = EncodingChecker(listOf()).getCharset(html)
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
        val charsets = EncodingChecker(listOf()).getCharset(html)
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
        val charsets = EncodingChecker(listOf()).getCharset(html)
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
        val charsets = EncodingChecker(listOf()).getCharset(html)
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
            on { getCharset("") } doReturn setOf<String>()
        }
//        val mock = org.mockito.Mockito.mock(EncodingChecker::class.java)
//        ::class.java)
//        `when`(mock.getCharset("")).thenReturn(setOf<String>())
//        val classUnderTest = ClassUnderTest(mock)
        assertTrue(mock.getCharset("").isEmpty())
//        `when`(checker.check("")).thenReturn(listOf())
//        val classUnderTest = ClassUnderTest(mock)
//
//        /* When */
//        classUnderTest.doAction()
//
//        /* Then */
//        verify(mock).doSomething(any())
    }

}