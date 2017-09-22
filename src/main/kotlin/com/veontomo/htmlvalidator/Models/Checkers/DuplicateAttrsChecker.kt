package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.html.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.jsoup.nodes.Element

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val stream = CharStreams.fromString(html)
        val lexer =  HTMLLexer(stream)
        val tokenStream =  CommonTokenStream(lexer);
        val parser =  HTMLParser(tokenStream);
        val  tree = parser.htmlElement()
        val result =  HTML().visit(tree)
        return listOf(CheckMessage(result, false))

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