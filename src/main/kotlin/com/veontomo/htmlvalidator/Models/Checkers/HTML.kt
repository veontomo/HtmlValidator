package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.Models.HtmlNode
import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor
import com.veontomo.htmlvalidator.html.HTMLParserVisitor
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.TerminalNode

class HTML : HTMLParserBaseVisitor<HtmlDocument>() {

    private fun constructTree(elements: List<HTMLParser.HtmlElementsContext>): List<HtmlNode> {
        return elements.map { it -> HtmlNode(it.htmlElement().htmlTagName().joinToString { it.text }, listOf()) }
    }

    override fun visitHtmlDocument(ctx: HTMLParser.HtmlDocumentContext?): HtmlDocument {
        val dtd = ctx?.dtd()?.DTD()?.text ?: ""
        val xml = ctx?.xml()?.XML_DECLARATION()?.text ?: ""
        val scriptlets = ctx?.scriptlet()?.map { it -> it.text } ?: listOf()
        val htmlElements = ctx?.htmlElements() ?: listOf()
        return HtmlDocument(dtd = dtd, xml = xml, scripts = scriptlets, nodes = constructTree(htmlElements))
    }


}