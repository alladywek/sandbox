package challenge5

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isEqualTo

class GravitripsTest {

    @Test
    @DisplayName("getGridStatus() should return status 'Red plays next' if grid is empty")
    fun test1() {
        val inputFields = listOf(
                listOf(
                        ".......",
                        ".......",
                        ".......",
                        ".......",
                        ".......",
                        "......."),
                listOf(
                        ".......",
                        ".......",
                        ".......")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Red plays next")
            that(getGridStatus(inputFields[1])).isEqualTo("Red plays next")
        }
    }

    @Test
    @DisplayName("getGridStatus() should return status 'Red wins' or 'Yellow wins' when player uses DIAGONAL strategy")
    fun test2() {
        val inputFields = listOf(
                listOf(
                        ".......",
                        ".......",
                        "....r..",
                        "...ry..",
                        "..Ryr..",
                        ".ryyyr."),
                listOf(
                        ".......",
                        ".......",
                        "..r....",
                        "...ry..",
                        "..yyr..",
                        ".ryyyR."),
                listOf(
                        ".......",
                        ".......",
                        "...y...",
                        "...ry..",
                        "...ryy.",
                        "...rrrY"),
                listOf(
                        ".......",
                        ".......",
                        "....ry.",
                        "...rY..",
                        "...yrr.",
                        "..yrrry")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Red wins")
            that(getGridStatus(inputFields[1])).isEqualTo("Red wins")
            that(getGridStatus(inputFields[2])).isEqualTo("Yellow wins")
            that(getGridStatus(inputFields[3])).isEqualTo("Yellow wins")
        }
    }

    @Test
    @DisplayName("getGridStatus() should return status 'Red wins' or 'Yellow wins' when player uses HORIZONTAL strategy")
    fun test3() {
        val inputFields = listOf(
                listOf(
                        ".......",
                        ".......",
                        ".yyyY..",
                        "...ry..",
                        "...ryr.",
                        "...rrry"),
                listOf(
                        ".......",
                        ".......",
                        "..y.y..",
                        "...ry..",
                        "...ryy.",
                        "..Rrrry")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Yellow wins")
            that(getGridStatus(inputFields[1])).isEqualTo("Red wins")
        }
    }

    @Test
    @DisplayName("getGridStatus() should return status 'Red wins' or 'Yellow wins' when player uses VERTICAL strategy")
    fun test4() {
        val inputFields = listOf(
                listOf(
                        ".......",
                        ".......",
                        ".R.....",
                        ".r.....",
                        ".ry....",
                        ".ryyy.."),
                listOf(
                        "rrryyyr",
                        "ryyrrry",
                        "yrryyyr",
                        "yyyrrry",
                        "Yrryyyr",
                        "yyyrrry")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Red wins")
            that(getGridStatus(inputFields[1])).isEqualTo("Yellow wins")
        }
    }

    @Test
    @DisplayName("getGridStatus() should return status 'Draw' if grid doesn't have free cells")
    fun test5() {
        val inputFields = listOf(
                listOf(
                        "rrryyyr",
                        "yyyrrry",
                        "rrryyyr",
                        "yyyrrry",
                        "rrryyyr",
                        "yyyrrry")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Draw")
        }
    }

    @Test
    @DisplayName("getGridStatus() should return status 'Red plays next' or 'Yellow plays next'")
    fun test6() {
        val inputFields = listOf(
                listOf(
                        ".......",
                        ".......",
                        ".......",
                        "...ry..",
                        "...ryy.",
                        "..yrrrY"),
                listOf(
                        ".......",
                        ".......",
                        ".......",
                        "...ry..",
                        "..Rryy.",
                        "..yrrry")
        )
        expect {
            that(getGridStatus(inputFields[0])).isEqualTo("Red plays next")
            that(getGridStatus(inputFields[1])).isEqualTo("Yellow plays next")
        }
    }
}