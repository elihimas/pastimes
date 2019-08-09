package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.model.Cell
import com.elihimas.games.pastimes.model.CellState
import javax.inject.Inject


enum class Turn(val cellState: CellState) {
    FIRST_PLAYER(CellState.X_SELECTED), SECOND_PLAYER(CellState.O_SELECTED);

    fun nextTurn() = if (this == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER
}

class TicTacToeGameController @Inject constructor() {

    var currentTurn = Turn.FIRST_PLAYER

    fun onCellClicked(cell: Cell) {
        if (cell.cellState == CellState.EMPTY) {
            cell.cellState = currentTurn.cellState
            currentTurn = currentTurn.nextTurn()
        }
    }

}