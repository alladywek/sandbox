package challenge5

fun getGridStatus(fields: List<String>): String {
    val grid = Grid(fields.map(String::toList))
    return when {
        grid.containsOnly('.') -> "Red plays next"
        grid.hasSequenceOf(4, listOf('r', 'R')) -> "Red wins"
        grid.hasSequenceOf(4, listOf('y', 'Y')) -> "Yellow wins"
        'R' in grid -> "Yellow plays next"
        'Y' in grid -> "Red plays next"
        else -> "Draw"
    }
}

private fun Grid.hasSequenceOf(length: Int, Chars: List<Char>): Boolean {
    val hasSequence: (List<Char>) -> Boolean = { chars -> chars.windowed(length).any { it.all(Chars::contains) } }
    return (rows + columns + diagonalsWithLength(length)).any(hasSequence)
}

private class Grid(val rows: List<List<Char>>) {

    val columns: List<List<Char>> = 0.until(rows[0].size).map { columnIndex -> rows.map { it[columnIndex] } }
}

private fun Grid.containsOnly(char: Char): Boolean = rows.all { row -> row.all { it == char } }
private operator fun Grid.contains(char: Char): Boolean = rows.any { row -> row.any { it == char } }

private fun Grid.diagonalsWithLength(length: Int): List<List<Char>> {
    val diagonals = mutableListOf<List<Char>>()
    (0..rows.size - length).forEach { rowIndex ->
        (0..rows[0].size - length).forEach { columnIndex ->
            diagonals.add((0 until length).map { shift -> rows[rowIndex + shift][columnIndex + shift] })
        }
        ((length - 1) until rows[0].size).forEach { columnIndex ->
            diagonals.add((0 until length).map { shift -> rows[rowIndex + shift][columnIndex - shift] })
        }
    }
    return diagonals
}
