package com.veontomo.htmlvalidator.html

import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.TerminalNode

class HTML (): HTMLParserVisitor<String>{
    override fun visitHtmlDocument(ctx: HTMLParser.HtmlDocumentContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlElements(ctx: HTMLParser.HtmlElementsContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlElement(ctx: HTMLParser.HtmlElementContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlContent(ctx: HTMLParser.HtmlContentContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlAttribute(ctx: HTMLParser.HtmlAttributeContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlAttributeName(ctx: HTMLParser.HtmlAttributeNameContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlAttributeValue(ctx: HTMLParser.HtmlAttributeValueContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlTagName(ctx: HTMLParser.HtmlTagNameContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlChardata(ctx: HTMLParser.HtmlChardataContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlMisc(ctx: HTMLParser.HtmlMiscContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitHtmlComment(ctx: HTMLParser.HtmlCommentContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitXhtmlCDATA(ctx: HTMLParser.XhtmlCDATAContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDtd(ctx: HTMLParser.DtdContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitXml(ctx: HTMLParser.XmlContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitScriptlet(ctx: HTMLParser.ScriptletContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitScript(ctx: HTMLParser.ScriptContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitStyle(ctx: HTMLParser.StyleContext?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitChildren(node: RuleNode?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitTerminal(node: TerminalNode?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visit(tree: ParseTree?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitErrorNode(node: ErrorNode?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}