package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.html.CalculatorBaseVisitor
import com.veontomo.htmlvalidator.html.CalculatorParser


class Calculator : CalculatorBaseVisitor<Int>() {
    override fun visitInt(ctx: CalculatorParser.IntContext): Int? {
        val value = ctx.INT().text
        return Integer.valueOf(value)
    }

    override fun visitParenthesis(ctx: CalculatorParser.ParenthesisContext): Int? {
        return visit(ctx.expression())
    }

    override fun visitAddSub(ctx: CalculatorParser.AddSubContext): Int {
        if (ctx.ADD() != null) {
            return visit(ctx.left) + visit(ctx.right);
        } else {
            return visit(ctx.left) - visit(ctx.right);
        }
    }

    override fun visitMulDiv(ctx: CalculatorParser.MulDivContext): Int {
        if (ctx.MUL() != null) {
            return visit(ctx.left) * visit(ctx.right);
        } else {
            return visit(ctx.left) / visit(ctx.right);
        }
    }
}