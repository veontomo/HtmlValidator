package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor

class HTMLVisitor : HTMLParserBaseVisitor<HtmlDocument>() {

    private fun constructRoot(elem: HTMLParser.HtmlElementsContext): List<HtmlNode> {
//        println("Constructing root for ${elem.text}")
        val tagList = elem.htmlElement().htmlTagName().map { it.TAG_NAME().text }.toSet()

        check(tagList.size == 1, { "multiple tags: ${tagList.joinToString { it }} in ${elem.text}" })
        val tag = tagList.first()
        val children = elem.htmlElement().htmlContent().htmlElement()
        val n = HtmlNode(tag)
        children.forEach { it -> appendNode(n, it) }
        return listOf(n)
    }

    private fun appendNode(n: HtmlNode, element: HTMLParser.HtmlElementContext) {
//        println("append node: parent = ${n.name}, element = ${element.text}")
        val tag = element.htmlTagName().map { it.TAG_NAME().text }
        val numOfTags = tag.size
        check(numOfTags <= 2, { "tag size must be 0, 1 or 2, not $numOfTags" })
        if (numOfTags > 0) {
            if (numOfTags == 2) {
                check(tag[0] == tag[1], { "opening and closing tags must have the same name, instead in ${element.text} found ${tag[0]} and ${tag[1]} " })

            }
            val node = HtmlNode(tag[0])
            val children = element.htmlContent().htmlElement()
            n.appendChild(node)
            children.forEach { it -> appendNode(node, it) }
        }

    }


    override fun visitHtmlDocument(ctx: HTMLParser.HtmlDocumentContext?): HtmlDocument {
        val dtd = ctx?.dtd()?.DTD()?.text ?: ""
        val xml = ctx?.xml()?.XML_DECLARATION()?.text ?: ""
        val scriptlets = ctx?.scriptlet()?.map { it -> it.text } ?: listOf()
        val htmlElements = ctx?.htmlElements() ?: listOf()
        val numOfHtmlElements = htmlElements.size
        check(numOfHtmlElements <= 1, { "Root node must be unique, instead $numOfHtmlElements received" })
        val child = if (numOfHtmlElements == 1) constructRoot(htmlElements[0]) else listOf()
        return HtmlDocument(dtd = dtd, xml = xml, scripts = scriptlets, nodes = child)
    }


}