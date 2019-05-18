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

private fun translateToMatrix(fields: List<String>): Result {
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
    val variants = matrix.rows().flatMap { row -> row.windowed(4) }
    return when {
        variants.any { it.sum() == 41 } -> Result(matrix, "Red wins")
        variants.any { it.sum() == 401 } -> Result(matrix, "Yellow wins")
        else -> this
    }
}

private fun Result.checkWinnerByVertical(): Result {
    if (statusDetermined) return this
    val variants = matrix.columns().flatMap { column -> column.windowed(4) }
    return when {
        variants.any { it.sum() == 41 } -> Result(matrix, "Red wins")
        variants.any { it.sum() == 401 } -> Result(matrix, "Yellow wins")
        else -> this
    }
}

private fun Result.checkWinnerByDiagonal(): Result {
    if (statusDetermined) return this
    val variants = matrix.diagonals().flatMap { diagonal -> diagonal.windowed(4) }
    return when {
        variants.any { it.sum() == 41 } -> Result(matrix, "Red wins")
        variants.any { it.sum() == 401 } -> Result(matrix, "Yellow wins")
        else -> this
    }
}

private fun Result.checkWhoNext(): Result = when {
    statusDetermined -> this
    matrix.contains(RED_LAST.value) -> Result(matrix, "Yellow plays next")
    else -> Result(matrix, "Red plays next")
}

private class Result(val matrix: Matrix, val status: String = "") {
    val statusDetermined: Boolean = status.isNotEmpty()
}

private class Matrix(private val rows: Int, private val columns: Int) {
    private val src: Array<Int> = Array(rows * columns) { NOTHING.value }
    private val srcTransparent: Array<Int> = Array(rows * columns) { NOTHING.value }
    operator fun Array<Int>.get(row: Int, column: Int): Int = this[columns * row + column]
    operator fun set(row: Int, column: Int, value: Int) {
        src[columns * row + column] = value
        srcTransparent[rows * column + row] = value
    }

    operator fun contains(value: Int): Boolean = value in src
    fun rows(): List<List<Int>> = src.toList().windowed(columns, columns)
    fun columns(): List<List<Int>> = srcTransparent.toList().windowed(rows, rows)
    fun diagonals(): List<List<Int>> {
        val diagonalsH = columns - 4
        val diagonalsR = rows - 4
        val result = mutableListOf<List<Int>>()
        (0..diagonalsH).forEach { column ->
            (0..diagonalsR).forEach { row ->
                result.add(listOf(
                        src[row, column],
                        src[row + 1, column + 1],
                        src[row + 2, column + 2],
                        src[row + 3, column + 3]
                ))
                result.add(listOf(
                        src[row, column + 3],
                        src[row + 1, column + 2],
                        src[row + 2, column + 1],
                        src[row + 3, column]
                ))
            }
        }
        return result
    }
}

private enum class Values(val value: Int) {
    NOTHING(0),
    RED(10),
    RED_LAST(11),
    YELLOW(100),
    YELLOW_LAST(101)
}

