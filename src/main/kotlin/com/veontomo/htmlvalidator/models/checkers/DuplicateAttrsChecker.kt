package com.veontomo.htmlvalidator.models.checkers

import com.veontomo.htmlvalidator.parser.DOM
import com.veontomo.htmlvalidator.parser.HtmlNode
import org.jsoup.nodes.Element

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val dom = try {
            DOM(html)
        } catch (e: IllegalStateException) {
            return listOf(CheckMessage("malformed document: ${e.message}", false))
        }

        val nodes = dom.nodes
        print(nodes[0].toString())

        return listOf(CheckMessage(dom.dtd, true))
    }

    /**
     * Find node's duplicate attributes.
     *
     * An attribute is considered as a duplicate if it is assigned more than once no matter to the same value or different.
     * @param node node to analyze
     * @return a map from a duplicate-valued attribute to a list of its values. Once present, the list must contain two elements or
     * more.
     */
    private fun findAttributeDuplicates(node: HtmlNode): Map<String, List<String>> {
        // stub
        return mapOf()
    }


    private fun inspectSingleNode(node: Element): Map<String, List<String>> {
        val attrs = node.attributes()
        val firstOccurrences = mutableMapOf<String, String>()
        val duplicates = mutableMapOf<String, MutableList<String>>()
        attrs.forEach { attr ->
            if (firstOccurrences.containsKey(attr.key)) {
                if (!duplicates.containsKey(attr.key)) {
                    duplicates.put(attr.key, mutableListOf(firstOccurrences[attr.key]!!))
                }
                duplicates[attr.key]!!.add(attr.value)
            } else {
                firstOccurrences.put(attr.key, attr.value)
            }
        }
        return duplicates
    }

    override val descriptor: String
        get() = "Duplicate attributes"
}