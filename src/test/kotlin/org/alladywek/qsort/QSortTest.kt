package org.alladywek.qsort

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


class QSortTest : StringSpec() {

    init {

        "should sort list of integers" {
            val instance = QSort()
            val myTable = table(
                    headers("inArr", "expectedArr"),
                    row(mutableListOf(8, 3, 5, 1, 0, 4, 3), mutableListOf(0, 1, 3, 3, 4, 5, 8)),
                    row(mutableListOf(1), mutableListOf(1)),
                    row(mutableListOf(), mutableListOf()),
                    row(mutableListOf(1, 2), mutableListOf(1, 2)),
                    row(mutableListOf(3, 1, 2), mutableListOf(1, 2, 3))
            )
            forAll(myTable) { a, b ->
                Gen.int()
                instance.sort(a)
                a shouldBe b
            }
        }
    }
}