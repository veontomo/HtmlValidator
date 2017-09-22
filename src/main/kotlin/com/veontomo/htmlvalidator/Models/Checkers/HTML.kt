package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor
import com.veontomo.htmlvalidator.html.HTMLParserVisitor
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.TerminalNode

class HTML: HTMLParserBaseVisitor<String>() {

    override fun visitHtmlAttribute(ctx: HTMLParser.HtmlAttributeContext?): String {
        return visitChildren(ctx).capitalize()
//        return super.visitHtmlAttribute(ctx)
    }
}