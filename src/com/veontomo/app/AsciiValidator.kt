package com.veontomo.app

import java.io.File
import java.util.*

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


    fun analyzeFile(file: File) {
        val invalid : HashMap<Int, String> = hashMapOf();
        file.readLines().forEachIndexed { i, s ->
            if (!isValid(s)) {
                invalid.put(i+1, s);
            }
        }
        if (invalid.size == 0){
            print("File " + file.name + " does not contain non-ascii characters.")
        } else {
            println("File " + file.name)
            invalid.forEach { i, s -> println("Line no. " + i + ": "+ s + " contains non-ascii characters."); }
        }

    }

}