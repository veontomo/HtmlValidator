package com.veontomo.app

/**
 * Created by Andrey on 07/06/2016.
 */
fun main(args: Array<String>) {
    val ascii = AsciiValidator();
    args.forEach  { it -> ascii.analyzeFile(it) }
}

