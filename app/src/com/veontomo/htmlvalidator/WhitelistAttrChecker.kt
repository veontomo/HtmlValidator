package com.veontomo.htmlvalidator

/**
 * Detect presence of attributes that are not in "white list".
 *
 * Every element is allowed to have attributes only from a list, called "white list".
 *
 *
 */
class WhitelistAttrChecker : Checker() {
    private val whitelist = listOf("title", "style", "href", "alt",
            "width", "max-width", "min-width",
            "padding", "padding-top", "padding-bottom", "padding-left", "padding-right",
            "margin", "margin-top", "margin-bottom", "margin-left", "margin-right",
            "text-decoration", "text-align", "line-height",
            "font-size", "font-weight", "font-family",
            "border", "border-style", "border-spacing",
            "color"
            )

    override fun check(html: String): List<CheckMessage> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}