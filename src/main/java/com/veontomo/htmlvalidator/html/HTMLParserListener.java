// Generated from HTMLParser.g4 by ANTLR 4.7
package com.veontomo.htmlvalidator.html;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HTMLParser}.
 */
public interface HTMLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void enterHtmlDocument(HTMLParser.HtmlDocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void exitHtmlDocument(HTMLParser.HtmlDocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlElements}.
	 * @param ctx the parse tree
	 */
	void enterHtmlElements(HTMLParser.HtmlElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlElements}.
	 * @param ctx the parse tree
	 */
	void exitHtmlElements(HTMLParser.HtmlElementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code full}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterFull(HTMLParser.FullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code full}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitFull(HTMLParser.FullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyWithSlash}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyWithSlash(HTMLParser.EmptyWithSlashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyWithSlash}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyWithSlash(HTMLParser.EmptyWithSlashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emtptyNoSlash}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterEmtptyNoSlash(HTMLParser.EmtptyNoSlashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emtptyNoSlash}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitEmtptyNoSlash(HTMLParser.EmtptyNoSlashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scriptTag1}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterScriptTag1(HTMLParser.ScriptTag1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code scriptTag1}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitScriptTag1(HTMLParser.ScriptTag1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code scriptTag2}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterScriptTag2(HTMLParser.ScriptTag2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code scriptTag2}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitScriptTag2(HTMLParser.ScriptTag2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code internalStyle}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterInternalStyle(HTMLParser.InternalStyleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code internalStyle}
	 * labeled alternative in {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitInternalStyle(HTMLParser.InternalStyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlContent}.
	 * @param ctx the parse tree
	 */
	void enterHtmlContent(HTMLParser.HtmlContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlContent}.
	 * @param ctx the parse tree
	 */
	void exitHtmlContent(HTMLParser.HtmlContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlAttribute}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttribute(HTMLParser.HtmlAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlAttribute}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttribute(HTMLParser.HtmlAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlAttributeName}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttributeName(HTMLParser.HtmlAttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlAttributeName}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttributeName(HTMLParser.HtmlAttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlAttributeValue}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttributeValue(HTMLParser.HtmlAttributeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlAttributeValue}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttributeValue(HTMLParser.HtmlAttributeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlTagName}.
	 * @param ctx the parse tree
	 */
	void enterHtmlTagName(HTMLParser.HtmlTagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlTagName}.
	 * @param ctx the parse tree
	 */
	void exitHtmlTagName(HTMLParser.HtmlTagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlChardata}.
	 * @param ctx the parse tree
	 */
	void enterHtmlChardata(HTMLParser.HtmlChardataContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlChardata}.
	 * @param ctx the parse tree
	 */
	void exitHtmlChardata(HTMLParser.HtmlChardataContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlMisc}.
	 * @param ctx the parse tree
	 */
	void enterHtmlMisc(HTMLParser.HtmlMiscContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlMisc}.
	 * @param ctx the parse tree
	 */
	void exitHtmlMisc(HTMLParser.HtmlMiscContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlComment}.
	 * @param ctx the parse tree
	 */
	void enterHtmlComment(HTMLParser.HtmlCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlComment}.
	 * @param ctx the parse tree
	 */
	void exitHtmlComment(HTMLParser.HtmlCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#xhtmlCDATA}.
	 * @param ctx the parse tree
	 */
	void enterXhtmlCDATA(HTMLParser.XhtmlCDATAContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#xhtmlCDATA}.
	 * @param ctx the parse tree
	 */
	void exitXhtmlCDATA(HTMLParser.XhtmlCDATAContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#dtd}.
	 * @param ctx the parse tree
	 */
	void enterDtd(HTMLParser.DtdContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#dtd}.
	 * @param ctx the parse tree
	 */
	void exitDtd(HTMLParser.DtdContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#xml}.
	 * @param ctx the parse tree
	 */
	void enterXml(HTMLParser.XmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#xml}.
	 * @param ctx the parse tree
	 */
	void exitXml(HTMLParser.XmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#scriptlet}.
	 * @param ctx the parse tree
	 */
	void enterScriptlet(HTMLParser.ScriptletContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#scriptlet}.
	 * @param ctx the parse tree
	 */
	void exitScriptlet(HTMLParser.ScriptletContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(HTMLParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(HTMLParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#style}.
	 * @param ctx the parse tree
	 */
	void enterStyle(HTMLParser.StyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#style}.
	 * @param ctx the parse tree
	 */
	void exitStyle(HTMLParser.StyleContext ctx);
}