package com.veontomo.htmlvalidator.Models.Checkers

import org.antlr.v4.parse.ANTLRLexer
import org.antlr.v4.runtime.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
//        val a = CharStreams.fromFileName("jdjj")
//        val b = ANTLRLexer(a)
        val doc = Jsoup.parse(html)
        val nodes = doc.allElements
        return nodes.map { node ->
            inspectSingleNode(node).flatMap { it ->
                listOf(CheckMessage("Tag ${node.text()}  has duplicate attribute ${it.key} with values ${it.value.joinToString { it }}", false))
            }
        }.flatten()
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