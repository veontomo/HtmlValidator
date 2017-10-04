package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.models.checkers.DuplicateAttrsChecker
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DuplicateAttrsCheckerTest {
    // Test suite for detecting duplicate attributes
    // Partition the input as follows
    // 1. total # attrs: 0, 1, 2, > 2
    // 2. # of different duplicate attrs: 0, 1, > 1
    // 3. values of the duplicates: equal, different, mixed

    // Cover
    // 1. total # attrs: 0
    // 2. # of different duplicate attrs: 0
    @Test
    fun `return empty list if the tag has no attributes`() {
        val input = "<!DOCTYPE html><html><body><div id=\"1\">block</div></body></html>"
        val checker = DuplicateAttrsChecker()
        val report = checker.check(input)
        assertEquals(0, report.size)
    }

    // Cover
    // 1. total # attrs: 1
    // 2. # of different duplicate attrs: 0
    @Test
    fun `return empty list if the tag has one attribute`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<div id=\"1\"></div>")
        assertEquals(0, report.size)
    }

    // Cover
    // 1. total # attrs: 2
    // 2. # of different duplicate attrs: 1
    // 3. values of the duplicates: different
    @Test
    fun `return one element list of the tag has two attributes and they have equal names with different values`() {
        val input = "<!DOCTYPE html><html><body><div><span duplattr=\"1\" duplattr=\"2\">block</div></body></html>"
        val checker = DuplicateAttrsChecker()
        val messages = checker.check(input)
        assertEquals(1, messages.size)
        assertTrue(messages[0].msg.contains("duplattr"))
    }

    // Cover
    // 1. total # attrs: 2
    // 2. # of different duplicate attrs: 1
    // 3. values of the duplicates: equal
    @Test
    fun `return one element list of the tag has two attributes and they have equal names with equal values`() {
        val checker = DuplicateAttrsChecker()
        val input = "<!DOCTYPE HTML>" +
                "<html> " +
                "<head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">" +
                "<title>AAA</title>" +
                "</head> <body>" +
                "<a href=\"http://www.example.com\" href=\"link\">a link</a> " +
                "<p>a paragraph</p> </body> </html>"
        val report = checker.check(input)
        assertEquals(1, report.size)
        assertTrue(report[0].msg.contains("href"))
        assertTrue(report[0].msg.contains("http://wwww.example.com"))
        assertTrue(report[0].msg.contains("link"))
        assertTrue(report[0].msg.contains(Regex("\ba\b")))
    }

    // Cover
    // 1. total # attrs: > 2
    // 2. # of different duplicate attrs: > 1
    // 3. values of the duplicates: mixed
    @Test
    fun `return two element list of the tag has six attributes, with two sets of duplicates of sizes 3 and 2`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<p class=\"first\" class=\"second\" width=\"220\" class=\"first\" width=\"240\" style=\"padding:0\">XYZ</p>")
        assertEquals(1, report.size)
    }

    /**
     * Test suite for detecting duplicate attributes.
     * Partition the input as follows:
     * 1. # nodes with duplicate attrs: 0, 1, > 1
     * 2. # duplicate attr values: 0, 2, > 2
     */


    /**
     * Cover:
     * 1. # nodes with duplicate attrs: 1
     * 2. # duplicate attr values: 2
     */
    @Test
    fun `return single element list if there is one node with double set attr`() {
    }
}