package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor
import com.veontomo.htmlvalidator.html.HTMLParserVisitor
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.TerminalNode

class HTML : HTMLParserBaseVisitor<String>() {

    override fun visitHtmlAttribute(ctx: HTMLParser.HtmlAttributeContext?): String {
        println("visiting an html attribute")
        println("attr name = ${ctx?.htmlAttributeName()?.TAG_NAME()}, value = ${ctx?.htmlAttributeValue()?.ATTVALUE_VALUE()}, tag equals = ${ctx?.TAG_EQUALS().toString()} ")
        return ctx?.let { super.visitHtmlAttribute(ctx) } ?: "???"
    }

    override fun visitDtd(ctx: HTMLParser.DtdContext?): String {
        println("visiting a dtd")
        return super.visitDtd(ctx)

    }

    override fun visitHtmlElements(ctx: HTMLParser.HtmlElementsContext?): String {
        println("visiting html elements: rule index ${ctx?.ruleIndex}")
        println("html elements: ${ctx?.htmlElement()?.children?.joinToString(transform = { it.text }, separator = " | ")}")
        return ctx?.let { super.visitHtmlElements(it) } ?: "---"
    }

    override fun visitHtmlElement(ctx: HTMLParser.HtmlElementContext?): String {
        println("visiting html element: rule index ${ctx?.ruleIndex}")
        println("html element: ${ctx?.htmlContent()?.children?.joinToString(transform = { it.text }, separator = ",")}")
        return ctx?.let { super.visitHtmlElement(it) } ?: "+++"
    }
}