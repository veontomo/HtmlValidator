package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Models.PlainAttrChecker
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.Test
import org.junit.Assert.*

/**
 * Test suit for plain attribute checker.
 */
class PlainAttrCheckerTest {
    /**
     * Tests for checking plain attributes.
     *
     * Partition the input as follows:
     * 1. # element's attrs: 0, 1, > 1
     * 2. # attrs in the set: 0, 1, > 1
     * 3. # overlapping attrs: 0, 1, > 1
     */
    // Cover:
    // 1. # element's attrs: 0
    // 2. # attrs in the set: 0
    // 3. # overlaps: 0
    @Test
    fun checkAttrAllEmpty() {
        val checker = PlainAttrChecker(setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        val messages = checker.controlPlainAttrs(el)
        assertTrue(messages.isEmpty())
    }

    // Cover:
    // 1. # element's attrs: 0
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrNoPlainTwoAllowed() {
        val checker = PlainAttrChecker(setOf("src", "title"))
        val el = Element(Tag.valueOf("div"), "")
        val messages = checker.controlPlainAttrs(el)
        assertTrue("element with no attr is a valid one", messages.isEmpty())
    }

    // Cover:
    // 1. # element's attrs: > 1
    // 2. # attrs in the set: 0
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsNoAllowed() {
        val checker = PlainAttrChecker(setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        el.attr("class", "wide")
        el.attr("title", "some dive")
        val messages = checker.controlPlainAttrs(el)
        assertEquals(2, messages.size)
        val pattern = Regex("\\bclass|title\\b")
        assertTrue(messages[0].contains(pattern))
        assertTrue(messages[1].contains(pattern))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsOneAllowed() {
        val checker = PlainAttrChecker(setOf("alt"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        val messages = checker.controlPlainAttrs(el)
        assertEquals(1, messages.size)
        assertTrue(messages[0].contains(Regex("\\btitle\\b")))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 1
    @Test
    fun checkAttrOneAttrOneAllowed() {
        val checker = PlainAttrChecker(setOf("title"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        val messages = checker.controlPlainAttrs(el)
        assertTrue("element with unique overlapping attr is a valid one", messages.isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsTwoAllowedNoOverlap() {
        val checker = PlainAttrChecker(setOf("alt", "href"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        val messages = checker.controlPlainAttrs(el)
        assertEquals(2, messages.size)
        val pattern = Regex("\\bwidth|title\\b")
        assertTrue(messages[0].contains(pattern))
        assertTrue(messages[1].contains(pattern))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 1 (partial)
    @Test
    fun checkAttrTwoAttrsTwoAllowedPartialOverlap() {
        val checker = PlainAttrChecker(setOf("width", "href"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        val messages = checker.controlPlainAttrs(el)
        assertEquals(1, messages.size)
        assertTrue(messages[0].contains(Regex("\\btitle\\b")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: > 1 (complete)
    @Test
    fun checkAttrTwoAttrsTwoAllowedCompleteOverlap() {
        val checker = PlainAttrChecker(setOf("width", "title"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        val messages = checker.controlPlainAttrs(el)
        assertTrue(messages.isEmpty())
    }
}

