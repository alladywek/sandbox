package org.alladywek.utils

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.FunSpec

class MathUtilsTest : FunSpec() {

    init {

        test("MathUtils.fromInfixToPostfixNotation() should return empty string if the expression is blank") {
            MathUtils.fromInfixToPostfixNotation("") shouldBe ""
            MathUtils.fromInfixToPostfixNotation("    ") shouldBe ""
        }

        test("MathUtils.fromInfixToPostfixNotation() throws IllegalArgumentException with message if the expression has illegal sign") {
            val testData = table(
                    headers("expression", "message"),
                    row("1 + 2a", "Unexpected sign [2a] in expression [1 + 2a]"),
                    row("x", "Unexpected sign [x] in expression [x]"),
                    row("abc + 1", "Unexpected sign [abc] in expression [abc + 1]"),
                    row("abc * 1", "Unexpected sign [abc] in expression [abc * 1]"),
                    row("1 / #", "Unexpected sign [#] in expression [1 / #]"),
                    row("abc + 1 - qwerty", "Unexpected sign [abc] in expression [abc + 1 - qwerty]")
            )
            forAll(testData) { expression, message ->
                val exception = shouldThrow<IllegalArgumentException> {
                    MathUtils.fromInfixToPostfixNotation(expression)
                }
                exception.message shouldBe message
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() should return postfix notation with '+' and '-' operations") {
            val testData = table(
                    headers("expression", "result"),
                    row("1 + 2", "1 2 +"),
                    row("1 - 2", "1 2 -"),
                    row("1.0 + 2.55", "1 2.55 +"),
                    row("0.99 + 2", "0.99 2 +"),
                    row("15.99 - 2", "15.99 2 -"),
                    row("   1 - 2", "1 2 -"),
                    row("1 - 2  ", "1 2 -"),
                    row("   1 - 2  ", "1 2 -"),
                    row("   1    -   2      ", "1 2 -")
            )
            forAll(testData) { expression, result ->
                MathUtils.fromInfixToPostfixNotation(expression) shouldEqual result
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() should return postfix notation with '*' and '/' operations") {
            val testData = table(
                    headers("expression", "result"),
                    row("1 * 2", "1 2 *"),
                    row("1 / 2", "1 2 /"),
                    row("1.0 * 2.55", "1 2.55 *"),
                    row("0.99 / 2", "0.99 2 /")
            )
            forAll(testData) { expression, result ->
                MathUtils.fromInfixToPostfixNotation(expression) shouldEqual result
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() should return postfix notation for complex expression") {
            val testData = table(
                    headers("expression", "result"),
                    row("1 * 2 + 2.2", "1 2 * 2.2 +"),
                    row("1 / 2 - 5", "1 2 / 5 -"),
                    row("4 - 1.0 * 2.55", "4 1 2.55 * -"),
                    row("1 + 0.99 / 2", "1 0.99 2 / +"),
                    row("1 + 0.99 / 2", "1 0.99 2 / +"),
                    row("( 1 + 0.99 ) / 2 + 0.8", "1 0.99 + 2 / 0.8 +"),
                    row("( 1 + 0.99 ) / ( 2 - 0.8 )", "1 0.99 + 2 0.8 - /"),
                    row("1 + 0.99 / 2", "1 0.99 2 / +"),
                    row("( 2 ^ 2 )", "2 2 ^"),
                    row("( 1 / 2 ) ^ 5", "1 2 / 5 ^"),
                    row("4 ^ 1.0  * 2.55", "4 1 ^ 2.55 *"),
                    row("1 ^ ( 2 - 5 )", "1 2 5 - ^")
            )
            forAll(testData) { expression, result ->
                MathUtils.fromInfixToPostfixNotation(expression) shouldEqual result
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() throws IllegalArgumentException if the expression contains inconsistent parentheses") {
            val testData = table(
                    headers("expression"),
                    row("( 1 + ( 2 + 3 )"),
                    row("( 5 + ( 2 + 3"),
                    row("1 + 7 + 3 )"),
                    row("1 + 2 ) + 8 )"),
                    row("( 1 + ) 0 + 3 )")
            )
            forAll(testData) { expression ->
                val exception = shouldThrow<IllegalArgumentException> {
                    MathUtils.fromInfixToPostfixNotation(expression)
                }
                exception.message shouldBe "The expression contains inconsistent parentheses"
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() should return postfix notation with parentheses") {
            val testData = table(
                    headers("expression", "result"),
                    row("( 2 + 2.2 )", "2 2.2 +"),
                    row("( 1 / 2 ) - 5", "1 2 / 5 -"),
                    row("( 4 - 1.0 ) * 2.55", "4 1 - 2.55 *"),
                    row("1 / ( 2 - 5 )", "1 2 5 - /")
            )
            forAll(testData) { expression, result ->
                MathUtils.fromInfixToPostfixNotation(expression) shouldEqual result
            }
        }

        test("MathUtils.fromInfixToPostfixNotation() should return postfix notation with exponentiation") {
            MathUtils.fromInfixToPostfixNotation("2 ^ 2") shouldEqual "2 2 ^"
        }
    }
}