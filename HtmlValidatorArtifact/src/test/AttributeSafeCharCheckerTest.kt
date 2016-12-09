package test

import com.veontomo.app.AttributeSafeCharChecker
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

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
     * 1. unsafe chars are present: true, false
     * 2. depth level of unsafe char location: 1 (head, html), 2 (meta, body), > 2 (some tag inside body)
     * 3. unsafe chars are escaped: true, false
     * 4. unsafe chars < (lt), > (gt), ... (hellip)
     * 5. html is valid: true, false
     */
    // Cover
    // 1. unsafe chars are present: false
    // 5. html is valid: true
    @Test
    fun checkAllSafe() {
        val html = "<!DOCTYPE HTML>" +
                "<html><body><div style=\"color: #ffffff;\">Hi!</div></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Document with no unsafe chars must produce no messages, instead got ${messages.joinToString { it.message }}", messages.isEmpty())
    }

    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: 1 (html)
    // 3. unsafe chars are escaped: false
    // 4. unsafe char: >
    // 5. html is valid: true
    @Test
    fun checkDepthLevelOneHtml() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta name=\"description\" content=\"test\"></head>" +
                "<html title=\"2 > 1\"><body></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Presence of \">\" in the \"html\" tag should have been reported", messages.isNotEmpty())
    }


    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: 1 (html)
    // 3. unsafe chars are escaped: true
    // 4. unsafe char: >
    // 5. html is valid: true
    @Test
    fun checkDepthLevelOneHtmlEscaped() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta http-equiv=\"content-type\" content=\"text/html; charset=ascii\"></head>" +
                "<html title=\"(escaped) 2 &gt; 1\"><body></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Escaped html entities must produce no messages, instead ${messages.joinToString { it.message }}", messages.isEmpty())
    }

    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: 2 (meta)
    // 3. unsafe chars are escaped: false
    // 4. unsafe char: <
    // 5. html is valid: true
    @Test
    fun checkDepthLevelTwoMeta() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta name=\"description\" content=\"test < safe\"></head>" +
                "<html><body></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Presence of \"<\" in the \"meta\" tag should have been reported.", messages.isNotEmpty())
    }

    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: 2 (meta)
    // 3. unsafe chars are escaped: true
    // 4. unsafe char: &hellip;
    // 5. html is valid: true
    @Test
    fun checkDepthLevelTwoMetaEscaped() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta name=\"description\" content=\"(escaped) test &hellip; safe\"></head>" +
                "<html><body></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Escaped html entities must produce no messages, instead ${messages.joinToString { it.message }}", messages.isEmpty())
    }

    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: > 2
    // 3. unsafe chars are escaped: true
    // 4. unsafe char: >
    // 5. html is valid: true
    @Test
    fun checkUnsafeInsideImgEscaped() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta name=\"description\" content=\"empty\"></head>" +
                "<html><body><img alt=\"(escaped) x &gt; y\"></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Escaped html entities must produce no messages", messages.isNotEmpty())
    }


    // Cover
    // 1. unsafe chars are present: true
    // 2. depth level: > 2
    // 3. unsafe chars are escaped: false
    // 4. unsafe char: <
    // 5. html is valid: true
    @Test
    fun checkUnsafeInsideSpan() {
        val html = "<!DOCTYPE HTML>" +
                "<head><meta name=\"description\" content=\"test < safe\"></head>" +
                "<html><body><p>A paragraph</p><span title=\" 1 < 2 \">A span</span></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Presence of \"<\" in the \"span\" tag should have been reported.", messages.isNotEmpty())
    }

    // Cover
    // 1. unsafe chars are present: false
    // 5. html is valid: false
    @Test
    fun checkInvalidHtml() {
        val html = "<!DOCTYPE HTML>" +
                "<html><body><p title=\"<br>\">A paragraph</p></body></html>"
        val messages = checker!!.check(html)
        assertTrue("Presence of a tag \"br\" inside a \"title\" attribute should have been reported: ${messages.joinToString { it.message }}", messages.isEmpty())
    }

}