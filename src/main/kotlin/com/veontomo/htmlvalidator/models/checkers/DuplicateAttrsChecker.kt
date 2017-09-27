package com.veontomo.htmlvalidator.models.checkers

import com.veontomo.htmlvalidator.parser.HtmlDocumentParser
import org.jsoup.nodes.Element

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val doc = try {
            HtmlDocumentParser().parse(html)
        } catch (e: IllegalStateException) {
            return listOf(CheckMessage("malformed document: ${e.message}", false))
        }

        return listOf(CheckMessage("not implemented yet", true))

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