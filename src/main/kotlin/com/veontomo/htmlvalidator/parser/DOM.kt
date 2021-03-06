package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLLexer
import com.veontomo.htmlvalidator.html.HTMLParser
import org.antlr.v4.gui.TreeViewer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.awt.ScrollPane
import javax.swing.JFrame

/**
 * An immutable ADT for representing a document object model (DOM) of an html document.
 * @param text string representation of the html document
 */
class DOM(private val text: String) {

    private val stream = CharStreams.fromString(text)
    private val lexer = HTMLLexer(stream)
    private val tokenStream = CommonTokenStream(lexer)
    private val parser = HTMLParser(tokenStream)
    private val tree = parser.htmlDocument()

    val dtd: String
        get() = tree.dtd()?.text ?: ""

    val nodes: List<HtmlNode> by lazy { getAllNodes() }

    override fun toString(): String {
        return dtd + nodes.map { it.toString() }
    }

    private fun getAllNodes(): List<HtmlNode> {
        return listOf(HtmlNodeParser().visit(tree))
    }


    init {
//        show AST in GUI
        val frame = JFrame("AST")
        val names = parser.ruleNames.map { it.toString() }
        val treeViewer = TreeViewer(names, tree)
        treeViewer.scale = 1.2
        frame.add(treeViewer)
        frame.setSize(1200, 800)
        frame.isVisible = true
    }

}