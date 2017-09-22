package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.html.CalculatorBaseVisitor
import com.veontomo.htmlvalidator.html.CalculatorParser



class Calculator: CalculatorBaseVisitor<Int>() {
    override fun visitInt(ctx: CalculatorParser.IntContext): Int? {
        val value = ctx.INT().text
        return Integer.valueOf(value)
    }
}