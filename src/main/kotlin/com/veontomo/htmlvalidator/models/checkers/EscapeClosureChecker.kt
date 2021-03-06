package com.veontomo.htmlvalidator.models.checkers

/**
 *  Control whether html escape sequences tarting with the ampersand (&)
 *  get closed with semicolon.
 */
class EscapeClosureChecker : Checker() {
    private val pattern = Regex("&[a-zA-Z#0-9]+(?!;)\\b")

    override fun check(html: String): List<CheckMessage> {
        val matches = pattern.findAll(html)
        return matches.toList().map{it -> it.value}.distinct().map { it ->  CheckMessage(it, false) }
    }


    override val descriptor: String
        get() = "& is closed by ;"
}