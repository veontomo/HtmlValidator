package com.veontomo.htmlvalidator.parser

import com.veontomo.htmlvalidator.html.HTMLLexer
import com.veontomo.htmlvalidator.html.HTMLParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

class HtmlDocumentBuilder {
    companion object {

        fun build(text: String): HtmlDocument {
            val stream = CharStreams.fromString(text)
            val lexer = HTMLLexer(stream)
            val tokenStream = CommonTokenStream(lexer)
            val parser = HTMLParser(tokenStream)
            val tree = parser.htmlDocument()
            //show AST in GUI
//            val frame = JFrame("AST")
//            val names = parser.ruleNames.map { it.toString() }
//            println("$names")
//            val treeViewer = TreeViewer(names, tree)
//            treeViewer.scale = 1.5
//            frame.add(treeViewer)
//            frame.setSize(640, 480)
//            frame.isVisible = true

            return HTMLVisitor().visit(tree)
        }

    }

    fun terminalNodeToString(ch: ParseTree): String {
        val s = ch.childCount
        if (s == 0) {
            return ch.text
        }
        var output = ""
        (0 until s).forEach { i ->
            output += terminalNodeToString(ch.getChild(i)) + "|"
        }
        return output
    }
}