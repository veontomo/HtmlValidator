package com.veontomo.app

import java.io.File


/**
 * Created by Andrey on 07/06/2016.
 */
fun main(args: Array<String>) {
    val ascii = AsciiValidator();
    val directory = File(args[0])
    val fList = directory.listFiles()
    fList.forEach { it -> ascii.analyzeFile(it) }

}

