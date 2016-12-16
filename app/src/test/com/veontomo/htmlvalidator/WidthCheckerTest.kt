package test.com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.WidthChecker
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.Before

/**
 * Test suite for WidthChecker.
 */
class WidthCheckerTest {
    private var checker: WidthChecker? = null

    @Before
    fun setUp() {
        checker = WidthChecker()
    }

    @After
    fun tearDown() {
        checker = null
    }

    /**
     * Test the method that checks whether the width is set in a consistent way.
     *
     * Partition the input as follows
     * 1. numerical value of the width attr: none, negative int, negative non-int, 0, positive int, positive non-int
     * 2. unit of measurement of the width attr: none, px, em, %
     * 3. width, min-width and max-width are present: true, false
     * 4. width, min-width and max-width have the same value: true, false
     */

    // Cover
    // 1. numerical value of the width attr: none
    // 2. unit of measurement of the width attr: none
    // 3. width, min-width and max-width are present: true
    // 4. width, min-width and max-width have the same value: true
    @Test
    fun isConsistentNoWidth() {
        val element = Element(Tag.valueOf("div"), "")
        assertTrue(checker!!.isConsistent(element))
    }

    // Cover
    // 1. numerical value of the width attr: positive
    // 2. unit of measurement of the width attr: none
    // 3. width, min-width and max-width are present: false
    // 4. width, min-width and max-width have the same value: false
    @Test
    fun isConsistentWidthNoMeasureNoInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10")
        assertFalse(checker!!.isConsistent(element))
    }

    // Cover
    // 1. numerical value of the width attr: positive
    // 2. unit of measurement of the width attr: em
    // 3. width, min-width and max-width are present: true
    // 4. width, min-width and max-width have the same value: true
    @Test
    fun isConsistentWidthWithMeasureInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10em")
        element.attr("style", "width: 10em; min-width: 10em; max-width: 10em;")
        assertFalse(checker!!.isConsistent(element))
    }

    // Cover
    // 1. numerical value of the width attr: positive
    // 2. unit of measurement of the width attr: no
    // 3. width, min-width and max-width are present: true
    // 4. width, min-width and max-width have the same value: true
    @Test
    fun isConsistentWidthNoMeasureInlineWidthPx() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10")
        element.attr("style", "width: 10px; min-width: 10px; max-width: 10px;")
        assertTrue(checker!!.isConsistent(element))
    }

    // Cover
    // 1. numerical value of the width attr: positive
    // 2. unit of measurement of the width attr: no
    // 3. width, min-width and max-width are present: true
    // 4. width, min-width and max-width have the same value: true
    @Test
    fun isConsistentWidthNoMeasureInlineWidthEm() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10")
        element.attr("style", "width: 10em; min-width: 10em; max-width: 10em;")
        assertTrue(checker!!.isConsistent(element))
    }

    @Test
    fun isConsistentWidthFractionalNoMeasureNoInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10.3")
        assertFalse(checker!!.isConsistent(element))
    }

    @Test
    fun isConsistentWidthFractionalWithMeasureNoInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "0.5%")
        assertFalse(checker!!.isConsistent(element))
    }

    /**
     * Test the method that selects given attributes from the element's style attribute
     *
     * Partition the input as follows:
     * 1. the number of keys in the style attr: 0, 1, > 1
     * 2. the number of attrs to select: 0, 1, > 1
     * 3. the number of overlapping attrs: 0, 1, > 1
     */
    // Cover:
    // 1. the number of keys in the style attr: 0
    // 2. the number of attrs to select: 0
    // 3. the number of overlapping attrs: 0
    @Test
    fun selectFrommStyleEmptyNothing() {
        val el = Element(Tag.valueOf("div"), "")
        val map = checker!!.selectFromStyle(el, listOf())
        assertTrue(map.isEmpty())
    }

    // Cover:
    // 1. the number of keys in the style attr: 0
    // 2. the number of attrs to select: 1
    // 3. the number of overlapping attrs: 0
    @Test
    fun selectFromStyleEmptyOne() {
        val el = Element(Tag.valueOf("div"), "")
        val map = checker!!.selectFromStyle(el, listOf("width"))
        assertTrue(map.isEmpty())
    }

    // Cover:
    // 1. the number of keys in the style attr: 1
    // 2. the number of attrs to select: 0
    // 3. the number of overlapping attrs: 0
    @Test
    fun selectFromStyleOneAttrNothing() {
        val el = Element(Tag.valueOf("div"), "")
        el.attr("style", "width:100px;")
        val map = checker!!.selectFromStyle(el, listOf("padding"))
        assertTrue(map.isEmpty())
    }

    // Cover:
    // 1. the number of keys in the style attr: 1
    // 2. the number of attrs to select: 1
    // 3. the number of overlapping attrs: 1
    @Test
    fun selectFromStyleOneAttrOneOverlap() {
        val el = Element(Tag.valueOf("div"), "")
        el.attr("style", "width:55px;")
        val map = checker!!.selectFromStyle(el, listOf("width"))
        assertEquals("The map must contain exactly one key", 1, map.size)
        assertEquals("The key \"width\" must have value \"55px\"", "55px", map["width"])
    }

    // Cover:
    // 1. the number of keys in the style attr: > 1
    // 2. the number of attrs to select: > 1
    // 3. the number of overlapping attrs: > 1
    @Test
    fun selectFromStyleOneAttrTwoOverlap() {
        val el = Element(Tag.valueOf("span"), "")
        el.attr("style", "padding: 5px; border-top: 10px solid #ABCDEF; margin: 10px;")
        val map = checker!!.selectFromStyle(el, listOf("border-top", "margin", "font-size"))
        assertEquals("The map must contain exactly two keys", 2, map.size)
        assertEquals("The key \"border-top\" must have value \"10px solid #ABCDEF\"", "10px solid #ABCDEF", map["border-top"])
        assertEquals("The key \"margin\" must have value \"10px\"", "10px", map["margin"])
    }

}