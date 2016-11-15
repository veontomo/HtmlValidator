package test

import com.veontomo.app.AttributeSafeCharChecker
import org.jsoup.Jsoup
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.w3c.dom.Attr

/**
 * Test the checker that detects a presence of non-safe chars inside values of html tag attributes.
 */
class AttributeSafeCharCheckerTest {

    private var checker: AttributeSafeCharChecker? = null


    @Before
    fun setUp() {
        checker = AttributeSafeCharChecker()
    }

    @After
    fun tearDown() {
        checker = null
    }

    /**
     * Test the method that finds non-safe chars inside attribute values
     * Partition the input as follows:
     * 1. # non-safe chars: 0, 1, > 1
     * 2. max num of non-safe chars inside the same attribute: 0, 1, > 1
     * 3. max num of non-safe chars inside the same tag: 0, 1, > 1
     * 4. presence of non-safe chars outside the attr values: yes, no
     *
     */
    // Cover
    // 1. # non-safe chars: 0
    // 4.  presence of non-safe chars outside the attr values: no
    @Test
    fun checkAllSafe() {
        val html = "<!DOCTYPE HTML> <html> <body> <div style=\"background-color: #ffffff; line-height: normal; text-align: center; font-size: 13px; width: 500px;\">hi</div></body></html>"
        assertTrue(checker!!.check(html).isEmpty())
    }


    // Cover
    // 1. # non-safe chars: 1 (asterisk)
    // 4.  presence of non-safe chars outside the attr values: no
    @Test
    fun checkAOneNonSafe() {
        val html = "<!DOCTYPE HTML> <html> <body> <div style=\"background-color: #ffffff; line-height: normal; text-align: 'center'; font-size: 13px; width: 500px;\">hi</div></body></html>"
        assertTrue(checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('<' and '>')
    // 2. max num of non-safe chars inside the same attr: 2
    // 3. max num of non-safe chars inside the same tag: 2
    // 4.  presence of non-safe chars outside the attr values: no
    @Test
    fun checkTwoNonSafeSameAttr() {
        val html = "<!DOCTYPE HTML> <html> <body> <img style=\"width: 500px;\" alt=\"image<br>description\">hi</div></body></html>"
        assertTrue(checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('à' and 'ù')
    // 2. max num of non-safe chars inside the same attr: 1
    // 3. max num of non-safe chars inside the same tag: 2
    // 4.  presence of non-safe chars outside the attr values: no
    @Test
    fun checkTwoNonSafeSameTagDifferentAttrs() {
        val html = "<!DOCTYPE HTML> <html> <body> <img style=\"width: 500px;\" class=\"ù\" alt=\"image à description\">hi</div></body></html>"
        assertTrue(checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe-chars: 1 ('è')
    // 2. max num of non-safe chars inside the same attr: 0
    // 3. max num of non-safe chars inside the same tag: 0
    // 4.  presence of non-safe chars outside the attr values: yes
    @Test
    fun checkTwoNonSafeOutsideAttributeValue() {
        val html = "<!DOCTYPE HTML> <html> <body> <img style=\"width: 500px;\" alt=\"image description\">Hi with è-symbol</div></body></html>"
        assertTrue(checker!!.check(html).isEmpty())
    }

    /**
     * Test the method that inspects the element attributes
     * Partition the input as follows:
     * 1. # attributes: 0, 1, > 1
     * 2. # attributes with non-safe chars: 0, 1, > 1
     */
    // Cover
    // 1. # attributes: 0
    // 2. # non-safe attr: 0
    @Test
    fun checkElementAttrsZeroAttributes() {
        val elem = Element(Tag.valueOf("span"), "simple span element")
        assertTrue(checker!!.checkElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: 1
    // 2. # non-safe attr: 0
    @Test
    fun checkElementAttrsOneAttrZeroAllSafe() {
        val attrs = Attributes()
        attrs.put(Attribute("style", "color:red;"))
        val elem = Element(Tag.valueOf("span"), "simple span element", attrs)
        assertTrue(checker!!.checkElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: > 1
    // 2. # non-safe attr: 0
    @Test
    fun checkElementAttrsTwoAttributesAllSafe() {
        val attrs = Attributes()
        attrs.put(Attribute("style", "color:red;"))
        attrs.put(Attribute("class", "wide"))
        val elem = Element(Tag.valueOf("div"), "a div element")
        assertTrue(checker!!.checkElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: 1
    // 2. # non-safe attr: 1
    @Test
    fun checkElementAttrsTwoAttributesOneNonSafe() {
        val attrs = Attributes()
        attrs.put(Attribute("style", "color:red;"))
        attrs.put(Attribute("alt", "wide<br>image"))
        val elem = Element(Tag.valueOf("img"), "", attrs)
        assertTrue(checker!!.checkElementAttributes(elem).isNotEmpty())
    }
    // Cover
    // 1. # attributes: > 1
    // 2. # non-safe attr: > 1
    @Test
    fun checkElementAttrsNoAttributes(){
        val attrs = Attributes()
        attrs.put(Attribute("style", "color:red;\r\n"))
        attrs.put(Attribute("class", "wide<br />"))
        val elem = Element(Tag.valueOf("div"), "a div element", attrs)
        assertTrue(checker!!.checkElementAttributes(elem).isNotEmpty())
    }
}