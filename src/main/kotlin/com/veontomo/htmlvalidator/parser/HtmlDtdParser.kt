package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLParser
import com.veontomo.htmlvalidator.html.HTMLParserBaseVisitor

class HtmlDtdParser : HTMLParserBaseVisitor<String>() {

    override fun visitDtd(ctx: HTMLParser.DtdContext?): String {
        return ctx?.DTD()?.text ?: ""
    }
}