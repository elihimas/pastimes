package com.elihimas.games.pastimes.model

import com.elihimas.games.pastimes.game.Cell
import com.elihimas.games.pastimes.game.TicTacToeSymbol


class TicTacToeTable {

    val cells = Array(3) { row ->
        Array(3) { column ->
            Cell(row, column)
        }
    }

    val firsRow by lazy {
        listOf(cells[0][0], cells[0][1], cells[0][2])
    }

    val secondRow by lazy {
        listOf(cells[1][0], cells[1][1], cells[1][2])
    }

    val thirdRow by lazy {
        listOf(cells[2][0], cells[2][1], cells[2][2])
    }

    val firsColumn by lazy {
        listOf(cells[0][0], cells[1][0], cells[2][0])
    }

    val secondColumn by lazy {
        listOf(cells[0][1], cells[1][1], cells[2][1])
    }

    val thirdColumn by lazy {
        listOf(cells[0][2], cells[1][2], cells[2][2])
    }

    val firstDiagonal by lazy {
        listOf(cells[0][0], cells[1][1], cells[2][2])
    }

    val secondDiagonal by lazy {
        listOf(cells[0][2], cells[1][1], cells[2][0])
    }

    fun reset() {
        cells.forEach { row ->
            row.forEach { cell ->
                cell.cellState = TicTacToeSymbol.EMPTY
            }
        }
    }
}
