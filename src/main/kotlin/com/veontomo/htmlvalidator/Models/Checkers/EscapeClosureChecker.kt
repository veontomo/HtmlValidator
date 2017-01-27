package com.veontomo.htmlvalidator.Models.Checkers

/**
 *  Control whether html escape sequences tarting with the ampersand (&)
 *  get closed with semicolon.
 */
class EscapeClosureChecker(): Checker(){
    override fun check(html: String): List<CheckMessage> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val descriptor: String
        get() = "& is closed by ;"
}