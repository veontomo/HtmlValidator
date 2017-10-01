package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor
import java.util.*

class HtmlNodeParser : HTMLParserBaseVisitor<HtmlNode>() {

    override fun visitHtmlElement(ctx: HTMLParser.HtmlElementContext?): HtmlNode {
        println("visiting html element ${ctx?.text}")
        val tags = ctx?.htmlTagName()?.map { it.TAG_NAME().text }
        check(tags != null, { "No tags are found in html element ${ctx?.text}" })
        val size = tags!!.size
        check(size < 3, { "Tag list must contain 0, 1 or 2 elements, instead it contains $size elements." })
        if (size == 2) {
            check(tags[0] == tags[1], { "Opening and closing tags must be equal, instead $tags[0] and $tags[1] are given." })
        }
        val tagName = tags[0]
        val attrs = ctx.htmlAttribute().map { Pair<String, String>(it.htmlAttributeName().text, it.htmlAttributeValue().text) }
        val node = HtmlNode(tagName)
        node.attributes = attrs

        val texts = ctx.htmlContent().htmlChardata()
        texts.forEach{
            val textNode = HtmlNode("foo")
            textNode.text = it.text
            node.appendChild(textNode)

        }
        println("element text: ${texts.joinToString { it.text }}")
        val elems = ctx.htmlContent()?.htmlElement()
        println("elements: ${elems?.joinToString { it.htmlContent().text }}")
        elems?.forEach {
            val n = visitHtmlElement(it)
            node.appendChild(n)
        }
        return node
    }

}