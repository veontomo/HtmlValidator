package com.veontomo.htmlvalidator.parser

/**
 * A mutable ADT representing an html document
 */
class HtmlNode(val name: String) {
    private val childNodes = mutableListOf<HtmlNode>()
    private val attrs = mutableListOf<Pair<String, String>>()
    private val style = mutableListOf<Pair<String, String>>()
    private var nodeText: String? = null

    var attributes: List<Pair<String, String>>
        get() = attrs
        set(value) {
            attrs.clear()
            value.forEach { attrs.add(Pair(it.first, it.second)) }
        }

    var text: String?
        get() = nodeText
        set(value) {
            if (attrs.isEmpty() && style.isEmpty() && childNodes.isEmpty()) {
                nodeText = value
            } else {
                throw IllegalStateException("Trying to set text content to a non-terminal node.")
            }
        }

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
        return text?.let { it } ?: "[$name] ${attrs.joinToString { "${it.first}=${it.second}" }} ${childNodes.map { it.toString() }.joinToString { it }}[/$name]"
    }


}