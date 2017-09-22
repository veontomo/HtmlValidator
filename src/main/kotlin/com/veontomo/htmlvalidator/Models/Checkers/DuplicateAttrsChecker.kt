package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.html.*
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val doc = Jsoup.parse(html)
        val inputStream = ANTLRInputStream("I would like to <i>emphasize</i> this and <b>underline</b> .")
        val markupLexer = HTMLLexer(inputStream)
        val commonTokenStream = CommonTokenStream(markupLexer)
        val markupParser = HTMLParser(commonTokenStream)

//        val fileContext = markupParser.context
//        val visitor = HTMLParserVisitor()
//        visitor.visit(fileContext)String expression = "42";
        val stream = CharStreams.fromString("<div>xxx</div>")
        val lexer =  HTMLLexer(stream)
        val tokenStream =  CommonTokenStream(lexer);
        val parser =  HTMLParser(tokenStream);
        val  tree = parser.htmlDocument()
        val result =  HTML().visit(tree);
        System.out.println(result); // prints "42";
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