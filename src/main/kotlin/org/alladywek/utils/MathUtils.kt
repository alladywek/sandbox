package org.alladywek.utils

import java.math.BigDecimal
import java.util.*

class MathUtils {

    companion object {

        @JvmStatic
        fun fromInfixToPostfixNotation(expression: String): String {
            if (expression.isBlank()) {
                return ""
            }
            return expression.toSigns().toPostfixNotation().buildString()
        }

        @JvmStatic
        fun calculateInfixExpression(expression: String): BigDecimal {
            if (expression.isBlank()) {
                return BigDecimal.ZERO
            }
            return expression.toSigns().toPostfixNotation().calculatePostfix()
        }
    }
}

sealed class Sign

open class Operation(val priority: Int) : Sign()

open class OperationWithAction(priority: Int, val action: (BigDecimal, BigDecimal) -> BigDecimal) : Operation(priority)

class OpeningParenthesis : Operation(0)

class ClosingParenthesis : Operation(0)

class Plus private constructor(priority: Int, action: (BigDecimal, BigDecimal) -> BigDecimal) : OperationWithAction(priority, action) {

    constructor() : this(1, { a: BigDecimal, b: BigDecimal -> a + b })

    override fun toString(): String {
        return "+"
    }
}

class Minus private constructor(priority: Int, action: (BigDecimal, BigDecimal) -> BigDecimal) : OperationWithAction(priority, action) {

    constructor() : this(1, { a: BigDecimal, b: BigDecimal -> a - b })

    override fun toString(): String {
        return "-"
    }
}

class Divide private constructor(priority: Int, action: (BigDecimal, BigDecimal) -> BigDecimal) : OperationWithAction(priority, action) {

    constructor() : this(2, { a: BigDecimal, b: BigDecimal -> a / b })

    override fun toString(): String {
        return "/"
    }
}

class Multiply private constructor(priority: Int, action: (BigDecimal, BigDecimal) -> BigDecimal) : OperationWithAction(priority, action) {

    constructor() : this(2, { a: BigDecimal, b: BigDecimal -> a * b })

    override fun toString(): String {
        return "*"
    }
}

class Power private constructor(priority: Int, action: (BigDecimal, BigDecimal) -> BigDecimal) : OperationWithAction(priority, action) {

    constructor() : this(3, { a: BigDecimal, b: BigDecimal -> BigDecimal(Math.pow(a.toDouble(), b.toDouble())).setScale(10, BigDecimal.ROUND_HALF_UP) })

    override fun toString(): String {
        return "^"
    }
}

class Number(value: BigDecimal) : Sign() {

    var value: BigDecimal = value
        private set

    override fun toString(): String {
        return value.stripTrailingZeros().toString()
    }
}

private fun String.toSigns(): List<Sign> {
    return this.split(Regex("\\s+")).filter(String::isNotBlank).map {
        when {
            it == "+" -> Plus()
            it == "-" -> Minus()
            it == "*" -> Multiply()
            it == "/" -> Divide()
            it == "(" -> OpeningParenthesis()
            it == ")" -> ClosingParenthesis()
            it == "^" -> Power()
            it.toDoubleOrNull() != null -> Number(BigDecimal(it).setScale(10, BigDecimal.ROUND_HALF_UP))
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
            is OpeningParenthesis -> stack.add(it)
            is ClosingParenthesis -> processCloseBracket(result, stack)
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
    if (stack.isNotEmpty() && stack.any { it is OpeningParenthesis }) {
        while (true) {
            val stackElement = stack.removeAt(stack.lastIndex)
            if (stackElement is OpeningParenthesis) return else result.add(stackElement)
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
    if (stack.any { it is OpeningParenthesis }) {
        throw IllegalArgumentException("The expression contains inconsistent parentheses")
    }
}

private fun List<Sign>.calculatePostfix(): BigDecimal {
    if (size == 1) {
        return (first() as Number).value.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
    }
    val list = LinkedList<Sign>(this)
    while (list.size > 1) {
        val index = list.indexOfFirst { it is Operation } - 2
        val val1 = list.removeAt(index) as Number
        val val2 = list.removeAt(index) as Number
        val operation = list[index] as OperationWithAction
        list[index] = Number(operation.action.invoke(val1.value, val2.value))
    }
    return (list.first() as Number).value.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
}