package com.veontomo.htmlvalidator.models.checkers

import com.veontomo.htmlvalidator.parser.DOM
import java.util.*

class DtdChecker : Checker() {
    val allowedDtds = setOf(
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">",
            "<!DOCTYPE html>").map { toCanonicalForm(it) }

    override fun check(html: String): List<CheckMessage> {
        val dom = try {
            DOM(html)
        } catch (e: IllegalStateException) {
            return listOf(CheckMessage(msg = "Invalid html document.", status = false))
        }
        val dtd = dom.dtd
        return if (isValidDtd(dtd)) {
            listOf()
        } else {
            listOf(CheckMessage(msg = "$dtd is not among allowed ones: ${allowedDtds.joinToString { it }}", status = false))
        }
    }

    /**
     * Return true if given dtd is a valid one. The validity of the input string
     * is checked by casting it into a canonical form and checking if it is present
     * among allowed dtd's.
     * @param dtd
     */
    private fun isValidDtd(dtd: String): Boolean {
        return allowedDtds.contains(toCanonicalForm(dtd))
    }

    private fun toCanonicalForm(text: String): String {
        return text.toLowerCase(Locale.ENGLISH).replace(Regex("\\s{2,}"), " ")
    }

    override val descriptor: String
        get() = "html dtd"
}