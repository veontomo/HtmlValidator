package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.Models.HtmlNode
import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor

class HTML : HTMLParserBaseVisitor<HtmlDocument>() {

    private fun constructRoot(elem: HTMLParser.HtmlElementsContext): List<HtmlNode> {
        println("Constructing root for ${elem.text}")
        val tagList = elem.htmlElement().htmlTagName().map { it.TAG_NAME().text }.toSet()

        check(tagList.size == 1, { "multiple tags: ${tagList.joinToString { it}} in ${elem.text}" })
        val tag = tagList.first()
        val children = elem.htmlElement().htmlContent().htmlElement()
        val n = HtmlNode(tag)
        children.forEach { it -> appendNode(n, it) }
        return listOf(n)
    }

    private fun appendNode(n: HtmlNode, element: HTMLParser.HtmlElementContext) {
        println("append node: parent = ${n.name}, element = ${element.text}")
        val tag = element.htmlTagName()
        check(tag.size == 2, { "tag size must be 1, not ${tag.size}" })
        val node = HtmlNode(tag[0].text)
        val children = element.htmlContent().htmlElement()
        n.appendChild(node)
        children.forEach { it -> appendNode(node, it) }

    }


    override fun visitHtmlDocument(ctx: HTMLParser.HtmlDocumentContext?): HtmlDocument {
        val dtd = ctx?.dtd()?.DTD()?.text ?: ""
        val xml = ctx?.xml()?.XML_DECLARATION()?.text ?: ""
        val scriptlets = ctx?.scriptlet()?.map { it -> it.text } ?: listOf()
        val htmlElements = ctx?.htmlElements() ?: listOf()
        check(htmlElements.size == 1, { "Root node must be unique, instead ${htmlElements.size} received" })
        println("html elements: ${htmlElements[0].text}")
        return HtmlDocument(dtd = dtd, xml = xml, scripts = scriptlets, nodes = constructRoot(htmlElements[0]))
    }


}