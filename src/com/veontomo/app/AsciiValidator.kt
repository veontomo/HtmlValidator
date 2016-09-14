package com.veontomo.app

import java.io.File
import java.util.*

/**
 * Check presence of non-ascii characters.
 */
class AsciiValidator {
    fun filterOutValid(text: String): List<Char> {
        return text.toCharArray().filter { c -> (c.toInt() < 32 && c.toInt() != 9) || c.toInt() > 126 }
    }

    fun analyzeFile(file: File) {
        println("File ${file.name} anomalies: ")
        file.readLines().forEachIndexed { i, s ->
            val res = filterOutValid(s)
            if (!res.isEmpty()) {
                print("line $i contains non-ascii symbols: ${res.joinToString { it -> "$it (ascii code: ${it.toInt()})" }}")
            }
        }
    }

}