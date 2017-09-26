package com.veontomo.htmlvalidator.Models.Checkers

import com.veontomo.htmlvalidator.Models.HtmlNode

class HtmlDocument(val dtd: String, val xml: String, val scripts: List<String>, val nodes: List<HtmlNode>) {
}