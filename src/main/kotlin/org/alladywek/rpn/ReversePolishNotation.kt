package org.alladywek.rpn

class ReversePolishNotation {

    companion object {

        private val regex: Regex = Regex("\\s+")

        @JvmStatic
        fun from(expression: String): String {
            if (expression.isBlank()) {
                return ""
            }
            val signs = arrayListOf<Sign>()
            expression.split(regex).filter(String::isNotBlank).forEach {
                when {
                    it == "+" -> signs.add(Plus())
                    it == "-" -> signs.add(Minus())
                    it == "*" -> signs.add(Multiply())
                    it == "/" -> signs.add(Divide())
                    it == "(" -> signs.add(OpenBracket())
                    it == ")" -> signs.add(CloseBracket())
                    it.toDoubleOrNull() != null -> signs.add(Number(it.toDouble()))
                    else -> throw IllegalArgumentException("Unexpected sign [$it] in expression [$expression]")
                }
            }
            return buildRPN(signs)
        }

        private fun buildRPN(signs: ArrayList<Sign>): String {
            val result = arrayListOf<Sign>()
            val stack = arrayListOf<Operation>()
            signs.forEach {
                when (it) {
                    is Number -> result.add(it)
                    is OpenBracket -> stack.add(OpenBracket())
                    is CloseBracket -> processCloseBracket(result, stack)
                    is Operation -> distributionOperation(result, stack, it)
                }
            }
            validateFinalStack(stack)
            result.addAll(stack.reversed())
            return result.joinToString(" ")
        }

        private fun processCloseBracket(result: ArrayList<Sign>, stack: ArrayList<Operation>) {
            if (stack.isNotEmpty() && stack.contains(OpenBracket())) {
                while (true) {
                    val stackElement = stack.removeAt(stack.lastIndex)
                    if (stackElement is OpenBracket) return else result.add(stackElement)
                }
            }
            throw IllegalArgumentException("The expression contains inconsistent parentheses")
        }

        private fun distributionOperation(result: ArrayList<Sign>, stack: ArrayList<Operation>, operation: Operation) {
            while (stack.isNotEmpty() && stack.last().priority >= operation.priority) {
                result.add(stack.removeAt(stack.lastIndex))
            }
            stack.add(operation)
        }

        private fun validateFinalStack(stack: ArrayList<Operation>) {
            if (stack.contains(OpenBracket())) {
                throw IllegalArgumentException("The expression contains inconsistent parentheses")
            }
        }
    }
}

sealed class Sign

open class Operation(val priority: Int) : Sign()

class Plus : Operation(1) {

    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a + b }

    override fun toString(): String {
        return "+"
    }
}

class Minus : Operation(1) {

    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a + b }

    override fun toString(): String {
        return "-"
    }
}

class Divide : Operation(2) {

    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a / b }

    override fun toString(): String {
        return "/"
    }
}

class Multiply : Operation(2) {

    val operation: (Double, Double) -> Double = { a: Double, b: Double -> a * b }

    override fun toString(): String {
        return "*"
    }
}

class OpenBracket : Operation(0) {

    override fun equals(other: Any?): Boolean {
        return other is OpenBracket
    }

    override fun toString(): String {
        return "("
    }

    override fun hashCode(): Int {
        return 1
    }
}

class CloseBracket : Operation(0) {

    override fun equals(other: Any?): Boolean {
        return other is CloseBracket
    }

    override fun toString(): String {
        return ")"
    }

    override fun hashCode(): Int {
        return 2
    }
}

data class Number(private val value: Double) : Sign() {
    override fun toString(): String {
        return if (value.toString().endsWith(".0")) value.toLong().toString() else value.toString()
    }
}
