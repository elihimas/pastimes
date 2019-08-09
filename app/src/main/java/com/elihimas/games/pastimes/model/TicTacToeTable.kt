package com.elihimas.games.pastimes.model

import com.elihimas.games.pastimes.R

enum class CellState(val cellResId: Int) {
    EMPTY(R.drawable.empty_cell_background), X_SELECTED(R.drawable.x_cell_background), O_SELECTED(R.drawable.o_cell_background);
}

data class Cell(val row: Int, val column: Int, var cellState: CellState = CellState.EMPTY)

class TicTacToeTable {

    val cells = Array(3) { row ->
        Array(3) { column ->
            Cell(row, column)
        }
    }


    fun onCellClicked(cell: Cell) {
        cell.cellState = when (cell.cellState) {
            CellState.EMPTY -> CellState.X_SELECTED
            CellState.X_SELECTED -> CellState.O_SELECTED
            CellState.O_SELECTED -> CellState.EMPTY
        }
    }

}
