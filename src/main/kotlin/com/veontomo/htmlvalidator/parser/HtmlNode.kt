package com.veontomo.htmlvalidator.parser

/**
 * A mutable ADT representing an html document
 */
class HtmlNode(val name: String) {
    private val childNodes = mutableListOf<HtmlNode>()

    fun appendChild(c: HtmlNode) {
        childNodes.add(c)
    }

    override fun toString(): String {
        return "<$name> ${childNodes.map { it.toString() }.joinToString { it }}</$name>"
    }
}