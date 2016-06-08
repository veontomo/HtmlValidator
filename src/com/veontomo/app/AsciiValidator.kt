package com.veontomo.app

/**
 * Created by Andrey on 07/06/2016.
 */
class AsciiValidator {

    fun isValid(text: String): Boolean {
        return text.toCharArray().map { x -> x.toInt() }.all {
            x ->
            x >= 32 && x <= 126
        }

    }
}