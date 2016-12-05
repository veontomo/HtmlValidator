package com.veontomo.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Entities
import org.jsoup.parser.Parser
import org.w3c.dom.Document
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Find a non-safe characters inside attribute values of html tags.
 *
 * Only these characters are considered safe for an html tag attribute value:
 * 0-9, a-z, A-Z, ;, :, ., comma, space, &, #, !, ?, -, +, =, /, $, %, (, ), _
 *
 */
class AttributeSafeCharChecker : Checker() {

    private val safeChars = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz;:.,&#!?-+=/$%()_".toSet()
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param html string whose validity is to be checked. Assume it is a valid html document.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(html: String): List<CheckMessage> {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(html)
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName())
//        val stream = html.byteInputStream(Charset.forName("ASCII"))
//        val doc = Jsoup.parse(stream, "ASCII", "")
//        val doc = Jsoup.parse(html, "ASCII", Parser.xmlParser())

//        doc.charset(Charset.forName("ASCII"))
//        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml)
//        val body = doc.body()
//        val children = body.children()
        val messages = mutableListOf<CheckMessage>()
//        for (element in children) {
//            checkDeepElementAttributes(element, messages)
//        }
        return messages
    }

    /**
     * Find non-safe chars of attribute values of given element and its children.
     * @param element this element and all its children are to be inspected. Not to be modified.
     * @param messages list of messages. It is a kind of accumulator for storing messages related
     * to the inspection. It is supposed to be modified by this method.
     */
    private fun checkDeepElementAttributes(element: Element, messages: MutableList<CheckMessage>) {
        val elemMessages = checkShallowElementAttributes(element)
        if (elemMessages.isNotEmpty()) {
            messages.addAll(elemMessages)
        }
        val children = element.children()
        if (children.isNotEmpty()) {
            children.forEach { child -> checkDeepElementAttributes(child, messages) }
        }
    }

    /**
     * Check given element and return messages concerning non-safe characters in the element attribute values.
     * The method does not inspect attributes of the element's children.
     * A single message of the resulting list might refer to multiple issues found in the element attributes.
     * @param el element whose attributes are to be inspected
     * @return list of messages.
     */
    fun checkShallowElementAttributes(el: Element): List<CheckMessage> {
        val result = mutableListOf<CheckMessage>()
        for (attr in el.attributes()) {
            val key = attr.key
            val value = attr.value
            val unSafeChars = value.toCharArray().filterNot { c -> isSafeChar(c) }
            if (unSafeChars.isNotEmpty()) {
                result.add(CheckMessage("$key attribute value $value is not safe: ${unSafeChars.joinToString { it.toString() }}"))
            }
        }
        return result
    }


    /**
     * Whether the given char is safe for being inside an html tag attribute value or not.
     * The set of safe chars is defined in the description of the class.
     */
    fun isSafeChar(c: Char): Boolean = safeChars.contains(c)

}