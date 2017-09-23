package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.html.CalculatorBaseVisitor
import com.veontomo.htmlvalidator.html.CalculatorParser


class Calculator : CalculatorBaseVisitor<Int>() {
    override fun visitInt(ctx: CalculatorParser.IntContext): Int? {
        print("inside int: ")
        val value = ctx.INT().text
        println(value)
        return Integer.valueOf(value)
    }

    override fun visitParenthesis(ctx: CalculatorParser.ParenthesisContext): Int? {
        println("inside parenthesis")
        return visit(ctx.expression())
    }

    override fun visitAddSub(ctx: CalculatorParser.AddSubContext): Int {
        println("inside visit add sub")
        if (ctx.ADD() != null) {
            println("it is add")
            return visit(ctx.left) + visit(ctx.right);
        } else {
            println("it is sub")
            return visit(ctx.left) - visit(ctx.right);
        }
    }

    override fun visitMulDiv(ctx: CalculatorParser.MulDivContext): Int {
        println("inside visit mul div")
        if (ctx.MUL() != null) {

            println("it is mul")
            return visit(ctx.left) * visit(ctx.right);
        } else {
            println("it is div")
            return visit(ctx.left) / visit(ctx.right);
        }
    }
}