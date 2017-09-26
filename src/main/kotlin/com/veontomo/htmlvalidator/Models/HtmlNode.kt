package com.veontomo.htmlvalidator.Models

/**
 * A mutable ADT representing an html document
 */
class HtmlNode(val name: String, val children: List<HtmlNode>) {
    private val childNodes = mutableListOf<HtmlNode>()

    init {
        children.forEach { child -> childNodes.add(child) }
    }

    fun appendChild(c: HtmlNode){
        childNodes.add(c)
    }
}