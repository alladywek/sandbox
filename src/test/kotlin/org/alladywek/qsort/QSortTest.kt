package org.alladywek.qsort

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.properties.Gen
import io.kotlintest.specs.BehaviorSpec


class QSortTest : BehaviorSpec() {

    init {

        Given("List of unsorted integer numbers") {
            val list = MutableList(1000) {
                 Gen.positiveIntegers().generate()
            }
            val expected = list.sorted()

            When("I try sort list of integer numbers") {
                QSort().sort(list)

                Then("It should be sorted") {
                    list shouldEqual expected
                }
            }
        }
    }
}