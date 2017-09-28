package com.veontomo.htmlvalidator.parser

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class HtmlDocumentParserTest {
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // Test suite for constructing an html tree
    // Partition the input as follows:
    // 1. dtd is present: yes, no
    // 2. script is present: yes, no
    // 3. # root nodes: 0, 1, > 1
    // 4. attributes are present: yes, no

    // Cover
    // 1. dtd is present: yes
    // 2. script is present: no
    // 3. # root nodes: 0
    // 4. attributes are present: no
    @Test
    fun `build a doc with dtd if input has dtd and no roots`() {
        val txt = "<!DOCTYPE html>"
        val doc = HtmlDocumentParser().parse(txt)
        assertEquals(0, doc.nodes.size)
        assertEquals("<!DOCTYPE html>", doc.dtd)
    }

    // Cover
    // 1. dtd is present: yes
    // 2. script is present: no
    // 3. # root nodes: 1
    // 4. attributes are present: no
    @Test
    fun `build a doc with dtd if input has dtd and one root`() {
        val txt = "<!DOCTYPE html><html></html>"
        val doc = HtmlDocumentParser().parse(txt)
        assertEquals(1, doc.nodes.size)
        assertEquals("html", doc.nodes[0].name)
        assertEquals("<!DOCTYPE html>", doc.dtd)
    }

    // Cover
    // 1. dtd is present: no
    // 2. script is present: no
    // 3. # root nodes: 1
    // 4. attributes are present: no
    @Test
    fun `build a doc without dtd if input has dtd and one root`() {
        val txt = "<html></html>"
        val doc = HtmlDocumentParser().parse(txt)
        assertEquals(1, doc.nodes.size)
        assertEquals("html", doc.nodes[0].name)
        assertEquals("", doc.dtd)
    }

    // Cover
    // 1. dtd is present: yes
    // 2. script is present: yes
    // 3. # root nodes: 1
    // 4. attributes are present: yes
    @Test
    fun `build a doc with dtd if input has htm4 dtd and one root with attributes`() {
        val txt = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html> <body> <a href=\"http://www.example.com\">a link</a> <p>a paragraph</p> </body> </html>"
        val doc = HtmlDocumentParser().parse(txt)
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", doc.dtd)
        assertEquals(1, doc.nodes.size)
        assertEquals("html", doc.nodes[0].name)
        assertEquals(1, doc.nodes[0].nodes.size)
        assertEquals("body", doc.nodes[0].nodes[0].name)
        assertEquals(2, doc.nodes[0].nodes[0].nodes.size)
        val link = doc.nodes[0].nodes[0].nodes[0]
        val para = doc.nodes[0].nodes[0].nodes[1]
        assertEquals("a", link.name)
        assertEquals(listOf("\"http://www.example.com\""), link.getAttribute("href"))
        assertEquals("p",para.name)
//        assertEquals(1, para.nodes.size)
        assertEquals("paragraph", para.text)
    }
}