package challenge5

import challenge5.Values.NOTHING
import challenge5.Values.RED
import challenge5.Values.RED_LAST
import challenge5.Values.YELLOW
import challenge5.Values.YELLOW_LAST

fun getGridStatus(fields: List<String>): String {
    return translateToMatrix(fields)
            .checkWinnerByHorizontal()
            .checkWinnerByVertical()
            .checkWinnerByDiagonal()
            .checkDrawResult()
            .checkWhoNext()
            .status
}

fun translateToMatrix(fields: List<String>): Result {
    val matrix = Matrix(fields.size, fields.first().length)
    fields.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            matrix[row, column] = when (char) {
                'r' -> RED.value
                'R' -> RED_LAST.value
                'y' -> YELLOW.value
                'Y' -> YELLOW_LAST.value
                else -> 0
            }
        }
    }
    return Result(matrix)
}

private fun Result.checkDrawResult(): Result {
    return if (!statusDetermined && NOTHING.value !in matrix) Result(this.matrix, "Draw") else this
}

private fun Result.checkWinnerByHorizontal(): Result {
    if (statusDetermined) return this
    return this
}

private fun Result.checkWinnerByVertical(): Result {
    if (statusDetermined) return this
    return this
}

private fun Result.checkWinnerByDiagonal(): Result {
    if (statusDetermined) return this
    return this
}

private fun Result.checkWhoNext(): Result = when {
    statusDetermined -> this
    matrix.contains(RED_LAST.value) -> Result(matrix, "Yellow plays next")
    else -> Result(matrix, "Red plays next")
}

class Result(val matrix: Matrix, val status: String = "") {
    val statusDetermined: Boolean = status.isNotEmpty()
}

class Matrix(private val rows: Int, private val columns: Int) {
    private val src: Array<Int> = Array(rows * columns) { NOTHING.value }
    operator fun get(row: Int, column: Int): Int = src[columns * row + column]
    operator fun set(row: Int, column: Int, value: Int) = src.set(columns * row + column, value)
    operator fun contains(value: Int): Boolean = value in src
}

enum class Values(val value: Int) {
    NOTHING(0),
    RED(10),
    RED_LAST(11),
    YELLOW(100),
    YELLOW_LAST(101)
}

