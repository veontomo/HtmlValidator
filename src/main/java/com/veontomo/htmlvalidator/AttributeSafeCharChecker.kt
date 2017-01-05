package com.veontomo.htmlvalidator

import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Parser
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Find a non-safe characters inside attribute values of an html document.
 *
 * Assume that the document contains only characters whose ascii codes are:
 *
 * 9 (\t), 10 (\n), 13 (\r), 32, ..., 126.
 *
 * Only these characters are considered safe for an html tag attribute value:
 *
 * 0-9, a-z, A-Z, ;, :, ., comma, space, &, #, !, ?, -, +, =, /, $, %, (, ), _
 *
 */
class AttributeSafeCharChecker : Checker() {
    override val descriptor = "Attribute safe char checker"

    private val safeChars = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz;:.,&#!?-+=/$%()_".toSet()
    /**
     * Check the validity of given string and return a list of messages related to found irregularities.
     * @param html string whose validity is to be checked. Assume it is a valid html document.
     * @return list of CheckMessage objects each of which reports an irregularity found in the input string.
     */
    override fun check(html: String): List<CheckMessage> {
        val messages = mutableListOf<CheckMessage>()
        val doc = Jsoup.parse(ByteArrayInputStream(html.toByteArray(Charset.forName("ascii"))), null, "")
        val items = doc.childNodes()
        items.forEach { node -> checkDeepElementAttributes(node, messages) }
        return messages
    }

    /**
     * Find non-safe chars of attribute values of given node and its children.
     * @param node this node and all its children are to be inspected. Not to be modified.
     * @param messages list of messages. It is a kind of accumulator for storing messages related
     * to the inspection. It is supposed to be modified by this method.
     */
    private fun checkDeepElementAttributes(node: Node, messages: MutableList<CheckMessage>) {
        val elemMessages = checkShallowElementAttributes(node)
        if (elemMessages.isNotEmpty()) {
            messages.addAll(elemMessages)
        }

        val children = node.childNodes()
        children.forEach { node -> checkDeepElementAttributes(node, messages) }
    }

    /**
     * Check given element and return messages concerning non-safe characters in the element attribute values.
     * The method does not inspect attributes of the element's children.
     * A single message of the resulting list might refer to multiple issues found in the element attributes.
     * @param node element whose attributes are to be inspected
     * @return list of messages.
     */
    private fun checkShallowElementAttributes(node: Node): List<CheckMessage> {
        val result = mutableListOf<CheckMessage>()
        if (node is TextNode) {
            return result
        }

        val attrs = node.attributes()
        for ((key, value) in attrs) {
            val unSafeChars = value.toCharArray().filterNot { c -> isSafeChar(c) }
            if (unSafeChars.isNotEmpty()) {
                result.add(CheckMessage("$key=\"$value\" contains unsafe chars: ${unSafeChars.joinToString{ it.toString() }}", false))
            }
        }
        return result
    }


    /**
     * Whether the given char is safe for being inside an html tag attribute value or not.
     * The set of safe chars is defined in the description of the class.
     */
    private fun isSafeChar(c: Char): Boolean = safeChars.contains(c)

}