package org.alladywek.sudoku

fun sudoku(init: Sudoku.() -> Unit): Sudoku {
    val sudoku = Sudoku()
    sudoku.init()
    return sudoku
}

class Sudoku {

    private val lines = arrayListOf<Line>()

    fun isSolved(): Boolean {
        return false
    }

    fun line(init: Line.() -> Unit): Line {
        val line = Line()
        line.init()
        lines.add(line)
        return line
    }

    fun isEmpty(): Boolean {
        return lines.isEmpty() || lines.all { it.isEmpty() }
    }
}

class Line {

    private val cells = arrayListOf<Cell>()

    fun cell(cellValue: Int = 0) {
        cells.add(Cell(cellValue))
    }

    fun isEmpty(): Boolean {
        return cells.isEmpty() || cells.all { it.isEmpty() }
    }
}

class Cell(private var value: Int) {

    fun isEmpty() = value == 0
}