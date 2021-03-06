package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.models.checkers.EscapeClosureChecker
import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore

/**
 * Test suit for the checker that detects missing final semicolon in html escape sequences.
 */
class EscapeClosureCheckerTest {
    @Test
    fun checkRegularHtml() {
        val html = "<!DOCTYPE html><html><head><title>Title</title></head><body></body><html>"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Test
    fun checkRegularString() {
        val html = "plain string"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Test
    fun checkTextWithFinalSemicolon() {
        val html = "1 &gt; 0"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Test
    fun checkHtmlWithFinalSemicolon() {
        val html = "<div>&egrave;</div>"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Test
    fun checkTextWithoutFinalSemicolon() {
        val html = "simple &aaa"
        assertTrue(EscapeClosureChecker().check(html).isNotEmpty())
    }

    @Ignore
    @Test
    fun checkHtmlWithoutFinalSemicolonAmp() {
        val html = "<div>&#039</div>"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Ignore
    @Test
    fun checkHtmlWithFinalSemicolonAmp() {
        val html = "<div>&#039;</div>"
        assertTrue(EscapeClosureChecker().check(html).isEmpty())
    }

    @Test
    fun checkHtmlWithoutFinalSemicolon() {
        val html = "<div>&egrave</div>"
        assertTrue(EscapeClosureChecker().check(html).isNotEmpty())
    }

    @Test
    fun getDescriptor() {
        assertTrue(EscapeClosureChecker().descriptor.isNotEmpty())
    }

}