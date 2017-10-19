package org.alladywek.utils

import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_UP
import java.math.BigDecimal.ZERO
import java.util.*

class MathUtils {

    companion object {

        private val DEFAULT_SCALE: Int = 10

        fun fromInfixToPostfixNotation(expression: String): String {
            if (expression.isBlank()) {
                return ""
            }
            return expression.toSigns().toPostfixNotation().buildString()
        }

        fun calculateInfixExpression(expression: String, scale: Int = DEFAULT_SCALE): BigDecimal {
            if (expression.isBlank()) {
                return ZERO
            }
            validateScale(scale)
            return expression.toSigns().toPostfixNotation().calculatePostfix(scale)
        }

        private fun validateScale(scale: Int) {
            if (scale !in 0..128) throw IllegalArgumentException("Scale value should be integer in range: -1 < scale < 129")
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

    constructor() : this(2, { a, b -> if (b.compareTo(ZERO) != 0) a / b else throw ArithmeticException("Division by zero") })

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

    constructor() : this(3, { a: BigDecimal, b: BigDecimal -> BigDecimal(Math.pow(a.toDouble(), b.toDouble())) })

    override fun toString(): String {
        return "^"
    }
}

class Number(value: BigDecimal) : Sign() {

    private companion object { @JvmStatic private val DEFAULT_NUMBER_SCALE = 128 }

    var value: BigDecimal = value.setScale(DEFAULT_NUMBER_SCALE, ROUND_HALF_UP)
        private set
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
            it.toDoubleOrNull() != null -> Number(BigDecimal(it))
            else -> throw IllegalArgumentException("Unexpected sign [$it] in expression [$this]")
        }
    }
}

private fun List<Sign>.toPostfixNotation(): List<Sign> {
    val result = arrayListOf<Sign>()
    val stack = LinkedList<Operation>()
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
    return this.joinToString(" ") { (it as? Number)?.value?.stripTrailingZeros()?.toString() ?: it.toString() }
}

private fun BigDecimal.prettyScale(scale: Int): BigDecimal {
    return this.setScale(scale, ROUND_HALF_UP).stripTrailingZeros()
}

private fun processCloseBracket(result: ArrayList<Sign>, stack: LinkedList<Operation>) {
    if (stack.isNotEmpty() && stack.any { it is OpeningParenthesis }) {
        while (true) {
            val stackElement = stack.removeLast()
            if (stackElement is OpeningParenthesis) return else result.add(stackElement)
        }
    }
    throw IllegalArgumentException("The expression contains inconsistent parentheses")
}

private fun processOperation(result: ArrayList<Sign>, stack: LinkedList<Operation>, currentOperation: Operation) {
    while (stack.isNotEmpty() && (stack.last().priority >= currentOperation.priority)) {
        result.add(stack.removeLast())
    }
    stack.add(currentOperation)
}

private fun validateFinalStack(stack: List<Sign>) {
    if (stack.any { it is OpeningParenthesis }) {
        throw IllegalArgumentException("The expression contains inconsistent parentheses")
    }
}

private fun List<Sign>.calculatePostfix(scale: Int): BigDecimal {
    if (size == 1) {
        return (single() as Number).value.prettyScale(scale)
    }
    val list = LinkedList<Sign>(this)
    while (list.size > 1) {
        val index = list.indexOfFirst { it is Operation } - 2
        val val1 = list.removeAt(index) as Number
        val val2 = list.removeAt(index) as Number
        val operation = list[index] as OperationWithAction
        list[index] = Number(operation.action(val1.value, val2.value))
    }
    return (list.single() as Number).value.prettyScale(scale)
}