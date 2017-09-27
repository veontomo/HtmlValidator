package com.veontomo.htmlvalidator.parser

/**
 * A mutable ADT representing an html document
 */
class HtmlNode(val name: String) {
    private val childNodes = mutableListOf<HtmlNode>()
    private val attrs = mutableListOf<Pair<String, String>>()
    private val style = mutableListOf<Pair<String, String>>()

    val nodes: List<HtmlNode>
        get() = childNodes

    fun appendChild(c: HtmlNode) {
        childNodes.add(c)
    }

    fun appendAttribute(key: String, value: String) {
        attrs.add(Pair(key, value))
    }

    fun getAttribute(key: String): List<String> {
        return attrs.filter { it.first == key }.map { it.second }
    }

    fun getStyleAttribute(key: String): List<String> {
        return style.filter { it.first == key }.map { it.second }
    }

    override fun toString(): String {
        return "<$name> ${childNodes.map { it.toString() }.joinToString { it }}</$name>"
    }


}