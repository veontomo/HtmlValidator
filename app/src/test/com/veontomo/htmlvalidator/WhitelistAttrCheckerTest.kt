package test.com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.WhiteListAttrChecker
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Test suit for the white list attribute checker.
 */
class WhiteListAttrCheckerTest {

    var checker: WhiteListAttrChecker? = null

    @Before
    fun setUp() {
        checker = WhiteListAttrChecker()
    }

    @After
    fun tearDown() {
        checker = null
    }

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
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker!!.hasAttrsFrom(el, setOf()))
    }

    // Cover:
    // 1. # element's attrs: 0
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrNoPlainTwoAllowed() {
        val el = Element(Tag.valueOf("div"), "")
        assertTrue("element with no attr is a valid one", checker!!.hasAttrsFrom(el, setOf("src", "title")))
    }

    // Cover:
    // 1. # element's attrs: > 1
    // 2. # attrs in the set: 0
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsNoAllowed() {
        val el = Element(Tag.valueOf("div"), "")
        el.attr("class", "wide")
        el.attr("title", "some dive")
        assertFalse("element with 'class', 'title' attrs and no allowed attrs is not a valid one", checker!!.hasAttrsFrom(el, setOf()))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsOneAllowed() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        assertFalse("element with no overlapping attrs is a not valid one", checker!!.hasAttrsFrom(el, setOf("alt")))
    }

    // Cover:
    // 1. # element's attrs:  1
    // 2. # attrs in the set: 1
    // 3. # overlaps: 1
    @Test
    fun checkAttrOneAttrOneAllowed() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        assertTrue("element with unique overlapping attr is a valid one", checker!!.hasAttrsFrom(el, setOf("title")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 0
    @Test
    fun checkAttrTwoAttrsTwoAllowedNoOverlap() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        assertFalse(checker!!.hasAttrsFrom(el, setOf("alt", "href")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: 1 (partial)
    @Test
    fun checkAttrTwoAttrsTwoAllowedpartialOverlap() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        assertFalse(checker!!.hasAttrsFrom(el, setOf("width", "href")))
    }

    // Cover:
    // 1. # element's attrs:  > 1
    // 2. # attrs in the set: > 1
    // 3. # overlaps: > 1 (complete)
    @Test
    fun checkAttrTwoAttrsTwoAllowedCompleteOverlap() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("title", "some dive")
        el.attr("width", "20")
        assertTrue(checker!!.hasAttrsFrom(el, setOf("width", "title")))
    }
    @Test
    fun checkInline() {

    }

}