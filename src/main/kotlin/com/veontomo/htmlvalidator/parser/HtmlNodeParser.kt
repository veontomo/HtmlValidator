package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor

class HtmlNodeParser : HTMLParserBaseVisitor<HtmlNode>() {

    override fun visitHtmlElement(ctx: HTMLParser.HtmlElementContext?): HtmlNode {
        println("visiting html element ${ctx?.text}")
        val tags = ctx?.htmlTagName()
        println("tags: ${tags?.joinToString { it.TAG_NAME().text }}")
        val elems = ctx?.htmlContent()?.htmlElement()
        println("elements: ${elems?.joinToString { it.htmlContent().text }}")
        elems?.forEach { visitHtmlElement(it) }
        return HtmlNode("dumb")

    }
}