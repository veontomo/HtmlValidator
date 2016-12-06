package test

import com.veontomo.app.AttributeSafeCharChecker
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

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
     * 5. location of non-safe attr wrt to the body: external, internal
     * 6. escaped html entities: present, absent
     */
    // Cover
    // 1. # non-safe chars: 0
    // 4. presence of non-safe chars outside the attr values: no
    // 6. escaped html entities: absent
    @Test
    fun checkAllSafe() {
        val html = "<!DOCTYPE HTML>" +
                "<html> <body> <div style=\"background-color: #ffffff; line-height: normal; text-align: center; font-size: 13px; width: 500px;\">hi</div></body></html>"
        assertTrue(checker!!.check(html).isEmpty())
    }

    // Cover
    // 1. # non-safe chars: 0
    // 6. escaped html entities: present
    @Test
    fun checkSafeEscaped() {
        val html = "<!DOCTYPE HTML>"+
                "<html> <body> <div title=\"these dots 	&#8230; are escaped\">...</div></body></html>"
        val messages = checker!!.check(html)
        assertTrue("message list must be empty, instead got \"${messages.joinToString { it.message }}\"", messages.isEmpty())
    }


    // Cover
    // 1. # non-safe chars: 1 (asterisk)
    // 4.  presence of non-safe chars outside the attr values: no
    // 5. location: external
    // 6. escaped html entities: absent
    @Test
    fun checkAOneNonSafe() {
        val html = "<!DOCTYPE HTML><html><body>" +
                "<div style=\"background-color: #ffffff; line-height: normal; text-align: 'center'; font-size: 13px; width: 500px;\">hi</div>" +
                "</body></html>"
        assertTrue("The presence of the asterisks is not detected", checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('<' and '>')
    // 2. max num of non-safe chars inside the same attr: 2
    // 3. max num of non-safe chars inside the same tag: 2
    // 4.  presence of non-safe chars outside the attr values: no
    // 6. escaped html entities: absent
    @Test
    fun checkTwoNonSafeSameAttr() {
        val html = "<!DOCTYPE HTML> <html> <body> " +
                "<img style=\"width: 500px;\" alt=\"image<br>description\"/>" +
                "</body></html>"
        assertTrue("The presence of < and > is not detected.", checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('à' and 'ù')
    // 2. max num of non-safe chars inside the same attr: 1
    // 3. max num of non-safe chars inside the same tag: 2
    // 4.  presence of non-safe chars outside the attr values: no
    // 5. location: external
    // 6. escaped html entities: absent
    @Test
    fun checkTwoNonSafeSameTagDifferentAttrsExternal() {
        val html = "<!DOCTYPE HTML> <html> <body> " +
                "<img style=\"width: 500px;\" class=\"ù\" alt=\"image à description\" />" +
                "</body></html>"
        assertTrue("The presence of non-ascii characters (ù and à) is not detected", checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('à' and 'ù')
    // 2. max num of non-safe chars inside the same attr: 1
    // 3. max num of non-safe chars inside the same tag: 2
    // 4.  presence of non-safe chars outside the attr values: no
    // 5. location: internal
    // 6. escaped html entities: absent
    @Test
    fun checkTwoNonSafeSameTagDifferentAttrsInternal() {
        val html = "<!DOCTYPE HTML> <html> <body><div><span> <img style=\"width: 500px;\" class=\"ù\" alt=\"image à description\" />hi</span></div></body></html>"
        assertTrue(checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe chars: 2 ('à' and 'ù')
    // 2. max num of non-safe chars inside the same attr: 1
    // 3. max num of non-safe chars inside the same tag: 1
    // 4.  presence of non-safe chars outside the attr values: no
    // 5. location: internal
    // 6. escaped html entities: absent
    @Test
    fun checkTwoNonSafeDifferentTagDifferentAttrsInternal() {
        val html = "<!DOCTYPE HTML> <html> <body> <span><img style=\"width: 500px;\" class=\"ù\" alt=\"image à description\" />hi</span>" +
                "<a title=\"a<br>link\" href=\"link\"></div></body></html>"
        assertTrue(checker!!.check(html).isNotEmpty())
    }

    // Cover
    // 1. # non-safe-chars: 1 ('è')
    // 2. max num of non-safe chars inside the same attr: 0
    // 3. max num of non-safe chars inside the same tag: 0
    // 4.  presence of non-safe chars outside the attr values: yes
    // 6. escaped html entities: absent
    @Test
    fun checkOneNonSafeOutsideAttributeValue() {
        val html = "<!DOCTYPE HTML><html><body>" +
                "<span style=\"width: 500px;\" title=\"a span element\">Hi with è-symbol</span>" +
                "</body></html>"
        val messages = checker!!.check(html)

        assertTrue("No anomalies should be detected, instead ${messages.joinToString { it.message }}", messages.isEmpty())
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
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.newDocument()
        val elem = document.createElement("span")
//        val elem = Element(Tag.valueOf("span"), "simple span element")
        elem.setAttribute("style", "color:red")
        elem.setAttribute("class", "wide")
        assertTrue(checker!!.checkShallowElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: 1
    // 2. # non-safe attr: 0
    @Test
    fun checkElementAttrsOneAttrZeroAllSafe() {
//        val attrs = Attributes()
//        attrs.put(Attribute("style", "color:red;"))
//        val elem = Element(Tag.valueOf("span"), "simple span element", attrs)
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.newDocument()
        val elem = document.createElement("span")
        elem.setAttribute("style", "color:red")
        assertTrue(checker!!.checkShallowElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: > 1
    // 2. # non-safe attr: 0
    @Test
    fun checkElementAttrsTwoAttributesAllSafe() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.newDocument()
        val elem = document.createElement("div")
        elem.setAttribute("style", "color:red")
        elem.setAttribute("class", "wide")
//        val attrs = Attributes()
//        attrs.put(Attribute("style", "color:red;"))
//        attrs.put(Attribute("class", "wide"))
//        val elem = Node.(Tag.valueOf("div"), "a div element")
        assertTrue(checker!!.checkShallowElementAttributes(elem).isEmpty())
    }

    // Cover
    // 1. # attributes: 1
    // 2. # non-safe attr: 1
    @Test
    fun checkElementAttrsTwoAttributesOneNonSafe() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.newDocument()
        val elem = document.createElement("div")
        elem.setAttribute("style", "color:red")
        elem.setAttribute("class", "wide")
        elem.setAttribute("title", "element with<br>linebreak")
//        val attrs = Attributes()
//        attrs.put(Attribute("style", "color:red;"))
//        attrs.put(Attribute("alt", "wide<br>image"))
//        val elem = Element(Tag.valueOf("img"), "", attrs)
        assertTrue(checker!!.checkShallowElementAttributes(elem).isNotEmpty())
    }

    // Cover
    // 1. # attributes: > 1
    // 2. # non-safe attr: > 1
    @Test
    fun checkElementAttrsNoAttributes() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.newDocument()
        val elem = document.createElement("div")
        elem.setAttribute("style", "color:red;\r\n")
        elem.setAttribute("class", "wide<br />")
        assertTrue(checker!!.checkShallowElementAttributes(elem).isNotEmpty())
    }
}