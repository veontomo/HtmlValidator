package com.veontomo.htmlvalidator

import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.Assert.*
import org.junit.Test

/**
 * Test suite for the inline attribute checker.
 */
class InlineAttrCheckerTest {

    /**
     * Tests for checking inline attributes.
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
    fun checkInlineAttrAllEmpty() {
        val checker = InlineAttrChecker(setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs: 0
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrNoPlainTwoAllowed() {
        val checker = InlineAttrChecker(setOf("padding", "margin"))
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs: > 1
    // 2. # attrs in the set: 0
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsNoAllowed() {
        val checker = InlineAttrChecker(setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        el.attr("style", "text-decoration: underline; padding: 10px;")
        assertFalse(checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsOneAllowed() {
        val checker = InlineAttrChecker(setOf("padding"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "width: 25em;")
        assertFalse(checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 1
    @Test
    fun checkInlineAttrOneAttrOneAllowed() {
        val checker = InlineAttrChecker(setOf("margin"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "margin: 22em;")
        assertTrue(checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedNoOverlap() {
        val checker = InlineAttrChecker(setOf("text-align", "border-style"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "color: #000222; font-size: 10pt;")
        assertFalse(checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 1 (partial)
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedpartialOverlap() {
        val checker = InlineAttrChecker(setOf("color", "title"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "color: #000222; font-size: 10pt;")
        assertFalse(checker.controlInlineAttrs(el).isEmpty())
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: > 1 (complete)
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedCompleteOverlap() {
        val checker = InlineAttrChecker(setOf("font-size", "margin"))
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "font-size; 10px; margin: 0;")
        assertTrue(checker.controlInlineAttrs(el).isEmpty())
    }

}