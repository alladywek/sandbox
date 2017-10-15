package org.alladywek.rpn

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.FunSpec

class ReversePolishNotationTest : FunSpec() {

    init {

        test("ReversePolishNotation.from() should return empty string if expression is blank") {
            ReversePolishNotation.from("") shouldBe ""
            ReversePolishNotation.from("    ") shouldBe ""
        }

        test("ReversePolishNotation.from() should return RPN from expression") {
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
                ReversePolishNotation.from(expression) shouldEqual result
            }
        }

        test("ReversePolishNotation.from() throws IllegalArgumentException with message if expression has illegal sign") {
            val testData = table(
                    headers("expression", "message"),
                    row("1 + 2a", "Unexpected sign [2a] in expression [1 + 2a]"),
                    row("x", "Unexpected sign [x] in expression [x]"),
                    row("abc + 1", "Unexpected sign [abc] in expression [abc + 1]"),
                    row("abc + 1 - qwerty", "Unexpected sign [abc] in expression [abc + 1 - qwerty]")
            )
            forAll(testData) { expression, message ->
                val exception = shouldThrow<IllegalArgumentException> {
                    ReversePolishNotation.from(expression)
                }
                exception.message shouldBe message
            }
        }
    }
}