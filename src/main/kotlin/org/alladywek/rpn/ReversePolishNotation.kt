package org.alladywek.rpn

import java.util.regex.Pattern

class ReversePolishNotation {

    companion object {

        private val pattern: Pattern = Pattern.compile("\\s+")

        @JvmStatic
        fun from(statement: String): String {
            val signs = arrayListOf<Sign>()
            val splitStatement = statement.split(pattern)
            splitStatement.forEach {
                when {
                    it == "+" -> signs.add(Plus())
                    it == "-" -> signs.add(Minus())
                    it.toDoubleOrNull() != null -> signs.add(Number(it.toDouble()))
                    else -> throw IllegalArgumentException("Unexpected sign: $it")
                }
            }
            return buildRPN(signs)
        }

        private fun buildRPN(signs: ArrayList<Sign>): String {
            val result = arrayListOf<Sign>()
            val stack = arrayListOf<Sign>()
            signs.forEach {
                when (it) {
                    is Number -> result.add(it)
                    is Plus -> stack.add(it)
                    is Minus -> stack.add(it)
                }
            }
            result.addAll(stack)
            return result.joinToString(" ")
        }
    }
}

sealed class Sign

class Plus : Sign() {

    val priority = 1
    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a + b }

    override fun toString(): String {
        return "+"
    }
}

class Minus : Sign() {

    val priority = 1
    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a + b }

    override fun toString(): String {
        return "-"
    }
}

data class Number(private val value: Double) : Sign() {
    override fun toString(): String {
        return if (value.toString().endsWith(".0")) value.toLong().toString() else value.toString()
    }
}
