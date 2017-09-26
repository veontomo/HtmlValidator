package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.Models.HtmlNode
import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor

class HTML2 : HTMLParserBaseVisitor<HtmlNode>() {
//    override fun visitHtmlAttribute(ctx: HTMLParser.HtmlAttributeContext?): HtmlNode {
//        println("visiting an html attribute")
//        println("attr name = ${ctx?.htmlAttributeName()?.TAG_NAME()}, value = ${ctx?.htmlAttributeValue()?.ATTVALUE_VALUE()}, tag equals = ${ctx?.TAG_EQUALS().toString()} ")
//        return ctx?.let { super.visitHtmlAttribute(ctx) } ?: "???"
//    }
//
//    override fun visitHtmlElements(ctx: HTMLParser.HtmlElementsContext?): HtmlNode {
//        println("visiting html elements: rule index ${ctx?.ruleIndex}")
//        println("html elements: ${ctx?.htmlElement()?.children?.joinToString(transform = { it.text }, separator = " | ")}")
//        return ctx?.let { super.visitHtmlElements(it) } ?: "---"
//    }
//
//    override fun visitHtmlElement(ctx: HTMLParser.HtmlElementContext?): HtmlNode {
//        println("visiting html element: rule index ${ctx?.ruleIndex}")
//        println("html element: ${ctx?.htmlContent()?.children?.joinToString(transform = { it.text }, separator = ",")}")
//        return ctx?.let { super.visitHtmlElement(it) } ?: "+++"
//    }
}