package challenge5

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isEqualTo

class GravitripsTest {

    @Test
    @DisplayName("getGridStatus() should return correct status")
    fun test1() {
        expect {
            testData.forEach { (inputFields, expectedStatus) ->
                that(getGridStatus(inputFields)).isEqualTo(expectedStatus)
            }
        }
    }
}

private val regexFields = "\"[.RYry]+\"".toRegex()
private val regexStatus = "'(.+)'".toRegex()
val testData = arrayOf(
        """
            ".......",
            ".......",
            ".R.....",
            ".r.....",
            ".ry....",
            ".ryyy.." | 'Red wins'
        """,
        """
            ".......",
            ".......",
            ".......",
            ".yy....",
            ".rrRr..",
            ".ryyy.." | 'Red wins'
        """,
        """
            ".......",
            ".......",
            "....r..",
            "...ry..",
            "..Ryr..",
            ".ryyyr." | 'Red wins'
        """,
        """
            ".......",
            ".......",
            "...y...",
            "...ry..",
            "...ryy.",
            "...rrrY" | 'Yellow wins'
        """,
        """
            ".......",
            ".......",
            ".yyyY..",
            "...ry..",
            "...ryr.",
            "...rrry" | 'Yellow wins'
        """,
        """
            ".......",
            ".......",
            ".y..y..",
            "...ry..",
            "...ryy.",
            "..Rrrry" | 'Red wins'
        """,
        """
            ".......",
            ".......",
            ".......",
            "...ry..",
            "...ryy.",
            "..yrrrY" | 'Red plays next'
        """,
        """
            ".......",
            ".......",
            ".......",
            "...ry..",
            "..Rryy.",
            "..yrrry" | 'Yellow plays next'
        """,
        """
            "rrryyyr",
            "yyyrrry",
            "rrryyyr",
            "yyyrrry",
            "rrryyyr",
            "yyyrrry" | 'Draw'
        """,
        """
            "rrryyyr",
            "yyyrrry",
            "ryryyyr",
            "yyyrrry",
            "rrryyyr",
            "yyyrYry" | 'Yellow wins'
        """,
        """
            "rrrRyyr",
            "yyyrrry",
            "ryryyyr",
            "yyyrrry",
            "rrryyyr",
            "yyyrrry" | 'Red wins'
        """
).map { regexFields.findAll(it).map { it.value.removeSurrounding("\"") }.toList() to
        regexStatus.find(it)!!.value.removeSurrounding("'") }