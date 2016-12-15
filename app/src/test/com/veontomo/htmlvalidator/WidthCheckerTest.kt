package test.com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.WidthChecker
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
     * 3. numerical value of the inline width value: none, negative int, negative non-int, 0, positive int, positive non-int
     * 4. unit of measurement of the inline width attr: none, px, em, %
     * 5. numerical value of the inline max-width attr:  none, negative int, negative non-int, 0, positive int, positive non-int
     * 6. unit of measurement of the inline max-width attr: none, px, em, %
     * 7. numerical value of the inline min-width attr:  none, negative int, negative non-int, 0, positive int, positive non-int
     * 8. unit of measurement of the inline min-width attr: none, px, em, %
     */

    // Cover
    // 1. numerical value of the width attr: none, negative int, negative non-int, 0, positive int, positive non-int
    // 2. unit of measurement of the width attr: none, px, em, %
    // 3. numerical value of the inline width value: none, negative int, negative non-int, 0, positive int, positive non-int
    // 4. unit of measurement of the inline width attr: none, px, em, %
    // 5. numerical value of the inline max-width attr:  none, negative int, negative non-int, 0, positive int, positive non-int
    // 6. unit of measurement of the inline max-width attr: none, px, em, %
    // 7. numerical value of the inline min-width attr:  none, negative int, negative non-int, 0, positive int, positive non-int
    // 8. unit of measurement of the inline min-width attr: none, px, em, %
    @Test
    fun isConsistentNoWidth() {
        val element = Element(Tag.valueOf("span"), "")
        assertTrue(checker!!.isConsistent(element))
    }

    @Test
    fun isConsistentWidthNoMeasureNoInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10")
        assertFalse(checker!!.isConsistent(element))
    }

    @Test
    fun isConsistentWidthWithMeasureNoInlineWidth() {
        val element = Element(Tag.valueOf("div"), "")
        element.attr("width", "10em")
        assertFalse(checker!!.isConsistent(element))
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
}