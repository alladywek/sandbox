package org.alladywek.sudoku

import io.kotlintest.specs.FeatureSpec
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class SudokuTest : FeatureSpec() {

    init {
        feature("Sudoku builder") {
            scenario("isEmpty() should return true if building empty sudoku") {
                assertTrue(sudoku { }.isEmpty())
            }
        }

        feature("Sudoku resolver") {
            scenario("isResolved() should return false after initialization of Sudoku class without parameters") {
                assertFalse(Sudoku().isSolved())
            }
        }
    }
}