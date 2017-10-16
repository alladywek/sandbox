package org.alladywek.utils

class MathUtils {

    companion object {

        @JvmStatic
        fun fromInfixToPostfixNotation(expression: String): String {
            if (expression.isBlank()) {
                return ""
            }
            return expression.toSigns().toPostfixNotation().buildString()
        }
    }
}

sealed class Sign

open class Operation(val priority: Int) : Sign()

class OpenBracket : Operation(0) {

    override fun toString(): String {
        return "("
    }
}

class CloseBracket : Operation(0) {

    override fun toString(): String {
        return ")"
    }
}

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

class Power : Operation(3) {

    val operation: (Double, Double) -> Double = { a: Double, b: Double -> Math.pow(a, b) }

    override fun toString(): String {
        return "^"
    }
}

data class Number(private val value: Double) : Sign() {
    override fun toString(): String {
        return if (value.toString().endsWith(".0")) value.toLong().toString() else value.toString()
    }
}

private fun String.toSigns(): List<Sign> {
    return this.split(Regex("\\s+")).filter(String::isNotBlank).map {
        when {
            it == "+" -> Plus()
            it == "-" -> Minus()
            it == "*" -> Multiply()
            it == "/" -> Divide()
            it == "(" -> OpenBracket()
            it == ")" -> CloseBracket()
            it == "^" -> Power()
            it.toDoubleOrNull() != null -> Number(it.toDouble())
            else -> throw IllegalArgumentException("Unexpected sign [$it] in expression [$this]")
        }
    }
}

private fun List<Sign>.toPostfixNotation(): List<Sign> {
    val result = arrayListOf<Sign>()
    val stack = arrayListOf<Operation>()
    this.forEach {
        when (it) {
            is Number -> result.add(it)
            is OpenBracket -> stack.add(it)
            is CloseBracket -> processCloseBracket(result, stack)
            is Operation -> processOperation(result, stack, it)
        }
    }
    validateFinalStack(stack)
    return stack.reversed().toCollection(result)
}

private fun List<Sign>.buildString(): String {
    return this.joinToString(" ")
}

private fun processCloseBracket(result: ArrayList<Sign>, stack: ArrayList<Operation>) {
    if (stack.isNotEmpty() && stack.any { it is OpenBracket }) {
        while (true) {
            val stackElement = stack.removeAt(stack.lastIndex)
            if (stackElement is OpenBracket) return else result.add(stackElement)
        }
    }
    throw IllegalArgumentException("The expression contains inconsistent parentheses")
}

private fun processOperation(result: ArrayList<Sign>, stack: ArrayList<Operation>, currentOperation: Operation) {
    while (stack.isNotEmpty() && (stack.last().priority >= currentOperation.priority)) {
        result.add(stack.removeAt(stack.lastIndex))
    }
    stack.add(currentOperation)
}

private fun validateFinalStack(stack: List<Sign>) {
    if (stack.any { it is OpenBracket }) {
        throw IllegalArgumentException("The expression contains inconsistent parentheses")
    }
}