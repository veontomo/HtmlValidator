package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLLexer
import com.veontomo.htmlvalidator.html.HTMLParser
import org.antlr.v4.gui.TreeViewer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
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

    val dtd: String by lazy { extractDtd(text) }
    private var nodesPrivate = mutableListOf<HtmlNode>()


    private fun extractDtd(text: String): String {
        val tree = parser.dtd()
        val parser1 = HtmlDtdParser()
        return parser1.visit(tree)
    }

    val nodes: List<HtmlNode>
        get() = nodesPrivate


    init {

        val tree = parser.htmlDocument()
//        show AST in GUI
        val frame = JFrame("AST")
        val names = parser.ruleNames.map { it.toString() }
        println("$names")
        val treeViewer = TreeViewer(names, tree)
        treeViewer.scale = 1.5
        frame.add(treeViewer)
        frame.setSize(640, 480)
        frame.isVisible = true
        HtmlDocumentParser().visit(tree)
    }
}