package org.alladywek.utils

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.FeatureSpec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(KTestJUnitRunner::class)
class MathUtilsTest : FeatureSpec() {

    init {

        feature("MathUtils.fromInfixToPostfixNotation() functionality") {

            scenario("should return empty string if the expression is blank") {
                MathUtils.fromInfixToPostfixNotation("") shouldBe ""
                MathUtils.fromInfixToPostfixNotation("    ") shouldBe ""
            }

            scenario("throws IllegalArgumentException with message if the expression has illegal sign") {
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

            scenario("should return postfix notation with '+' and '-' operations") {
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

            scenario("should return postfix notation with '*' and '/' operations") {
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

            scenario("should return postfix notation for complex expression") {
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

            scenario("throws IllegalArgumentException if the expression contains inconsistent parentheses") {
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

            scenario("should return postfix notation considering parentheses") {
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

            scenario("should return postfix notation with exponentiation") {
                MathUtils.fromInfixToPostfixNotation("2 ^ 2") shouldEqual "2 2 ^"
            }
        }

        feature("MathUtils.calculateInfixExpression() functionality") {
            
            scenario("should return BigDecimal.Zero if expression is blank") {
                MathUtils.calculateInfixExpression("") shouldEqual BigDecimal.ZERO
                MathUtils.calculateInfixExpression(" ") shouldEqual BigDecimal.ZERO
                MathUtils.calculateInfixExpression("    ") shouldEqual BigDecimal.ZERO
            }

            scenario("should return the same value if expression contains just one number") {
                val testData = table (
                    headers("expression", "result"),
                    row("0.0", "0"),
                    row("0", "0"),
                    row("1.1", "1.1"),
                    row("-1.1", "-1.1"),
                    row("1", "1")
                )
                forAll(testData) { expression, result ->
                    assertTrue(MathUtils.calculateInfixExpression(expression) == BigDecimal(result).setScale(10).stripTrailingZeros())
                }
            }

            scenario("should calculate expression with '+' and '-'") {
                val testData = table (
                    headers("expression", "result"),
                    row("1 + 2", "3"),
                    row("5.0 - 4", "1"),
                    row("1 - 1.11", "-0.11"),
                    row("-2 + 2", "0"),
                    row("8 - 2 + 5.5", "11.5")
                )
                forAll(testData) { expression, result ->
                    assertTrue(MathUtils.calculateInfixExpression(expression) == BigDecimal(result).setScale(10).stripTrailingZeros())
                }
            }

            scenario("should calculate expression with '*' and '-'") {
                val testData = table (
                    headers("expression", "result"),
                    row("1 * 2", "2"),
                    row("5.0 / 4", "1.25"),
                    row("1 * 1.11", "1.11"),
                    row("-2 / 2", "-1"),
                    row("1 / 3", "0.3333333333"),
                    row("0.3333333333 * 3", "0.9999999999"),
                    row("8 / 2 * 5.5", "22")
                )
                forAll(testData) { expression, result ->
                    assertTrue(MathUtils.calculateInfixExpression(expression) == BigDecimal(result).setScale(10).stripTrailingZeros())
                }
            }

            feature("should calculate expression with '^'") {
                val testData = table (
                        headers("expression", "result"),
                        row("1 ^ 2", "1"),
                        row("5.0 ^ 4", "625"),
                        row("1 ^ 1.11", "1"),
                        row("-2 ^ 2", "4"),
                        row("-2 ^ 3", "-8"),
                        row("5 ^ 0", "1"),
                        row("5 ^ -2", "0.04"),
                        row("5 ^ 1.11", "5.9683832767"),
                        row("5 ^ -0", "1"),
                        row("0 ^ 2", "0")
                )
                forAll(testData) { expression, result ->
                    assertEquals(BigDecimal(result).setScale(10).stripTrailingZeros(), MathUtils.calculateInfixExpression(expression))
                }
            }
        }
    }
}