package com.elihimas.games.pastimes.model

import java.lang.IllegalStateException


data class TicTacToeCell(
    val row: Int,
    val column: Int,
    val table: TicTacToeTable,
    var cellSymbol: TicTacToeSymbol = TicTacToeSymbol.NONE
) {
    fun isTaken() = cellSymbol != TicTacToeSymbol.NONE
    fun isFree() = cellSymbol == TicTacToeSymbol.NONE
    fun neighbours() =
        sequence {
            if (row - 1 > 0) {
                yield(table.cells[row - 1][column])
            }
            if (row + 1 < 3) {
                yield(table.cells[row + 1][column])
            }
            if (column - 1 > 0) {
                yield(table.cells[row][column - 1])
            }
            if (column + 1 < 3) {
                yield(table.cells[row][column + 1])
            }
        }.toList()

    fun oppositeCorner() = if (this.row == 0 && this.column == 0) {
        table.cells[2][2]
    } else if (this.row == 2 && this.column == 2) {
        table.cells[0][0]
    } else if (this.row == 0 && this.column == 2) {
        table.cells[2][0]
    } else if (this.row == 2 && this.column == 0) {
        table.cells[0][2]
    } else {
        throw IllegalStateException("there is no opposite corner for ($row, $column)")
    }


    fun otherSide() = if (this.row == 0 && this.column == 1) {
        table.cells[2][1]
    } else if (this.row == 2 && this.column == 1) {
        table.cells[0][1]
    } else if (this.row == 1 && this.column == 0) {
        table.cells[1][2]
    } else if (this.row == 1 && this.column == 2) {
        table.cells[1][0]
    } else {
        throw IllegalStateException("there is no other side for ($row, $column)")
    }
}
