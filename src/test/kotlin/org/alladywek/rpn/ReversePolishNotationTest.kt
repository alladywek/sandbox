package org.alladywek.rpn

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
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
    }
}