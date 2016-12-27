package test.com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.WhiteListAttrChecker
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.Test
import org.junit.Assert.*

/**
 * Test suit for the white list attribute checker.
 */
class WhiteListAttrCheckerTest {
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
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("src", "title"), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("alt"), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("title"), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("alt", "href"), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("width", "href"), setOf<String>())
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
        val checker = WhiteListAttrChecker(setOf("width", "title"), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        val messages = checker.controlPlainAttrs(el)
        assertTrue(messages.isEmpty())
    }

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
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker.hasInlineAttrsFrom(el, setOf()))
    }

    // Cover:
    // 1. # element's attrs: 0
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrNoPlainTwoAllowed() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker.hasInlineAttrsFrom(el, setOf("padding", "margin")))
    }

    // Cover:
    // 1. # element's attrs: > 1
    // 2. # attrs in the set: 0
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsNoAllowed() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("div"), "")
        el.attr("style", "text-decoration: underline; padding: 10px;")
        assertFalse(checker.hasInlineAttrsFrom(el, setOf<String>()))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsOneAllowed() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "width: 25em;")
        assertFalse(checker.hasInlineAttrsFrom(el, setOf("padding")))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 1
    @Test
    fun checkInlineAttrOneAttrOneAllowed() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "margin: 22em;")
        assertTrue(checker.hasInlineAttrsFrom(el, setOf("margin")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedNoOverlap() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "color: #000222; font-size: 10pt;")
        assertFalse(checker.hasInlineAttrsFrom(el, setOf("text-align", "border-style")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 1 (partial)
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedpartialOverlap() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "color: #000222; font-size: 10pt;")
        assertFalse(checker.hasInlineAttrsFrom(el, setOf("color", "title")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: > 1 (complete)
    @Test
    fun checkInlineAttrTwoAttrsTwoAllowedCompleteOverlap() {
        val checker = WhiteListAttrChecker(setOf<String>(), setOf<String>())
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "font-size; 10px; margin: 0;")
        assertTrue(checker.hasInlineAttrsFrom(el, setOf("font-size", "margin")))
    }


}