package com.veontomo.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Entities
import org.jsoup.parser.Parser
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
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
        val messages = mutableListOf<CheckMessage>()
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        try {
            val doc = dBuilder.parse(InputSource(ByteArrayInputStream(html.toByteArray(Charset.forName("utf-8")))))
            val children = doc.childNodes
            val size = children.length
            for (i in 0..size-1) {
                checkDeepElementAttributes(children.item(i), messages)
            }
        } catch (e: Exception) {
            messages.add(CheckMessage(e.message ?: "Unknown error"))
        }
        return messages
    }

    /**
     * Find non-safe chars of attribute values of given element and its children.
     * @param element this element and all its children are to be inspected. Not to be modified.
     * @param messages list of messages. It is a kind of accumulator for storing messages related
     * to the inspection. It is supposed to be modified by this method.
     */
    private fun checkDeepElementAttributes(element: Node, messages: MutableList<CheckMessage>) {
        val elemMessages = checkShallowElementAttributes(element)
        if (elemMessages.isNotEmpty()) {
            messages.addAll(elemMessages)
        }
        val children = element.childNodes
        val size = children.length
        for (i in 0..size-1) {
            checkDeepElementAttributes(children.item(i), messages)
        }
    }

    /**
     * Check given element and return messages concerning non-safe characters in the element attribute values.
     * The method does not inspect attributes of the element's children.
     * A single message of the resulting list might refer to multiple issues found in the element attributes.
     * @param el element whose attributes are to be inspected
     * @return list of messages.
     */
    fun checkShallowElementAttributes(el: Node): List<CheckMessage> {
        val result = mutableListOf<CheckMessage>()
        val attrs = el.attributes
        val size = attrs?.length ?: 0
        for (i in 0..size-1) {
            val key = attrs.item(i).nodeName
            val value = attrs.item(i).nodeValue
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