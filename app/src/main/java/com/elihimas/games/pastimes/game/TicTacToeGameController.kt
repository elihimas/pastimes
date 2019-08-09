package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.TicTacToeTable
import javax.inject.Inject


enum class Turn(val cellState: TicTacToeSymbol) {
    FIRST_PLAYER(TicTacToeSymbol.X_SYMBOL), SECOND_PLAYER(TicTacToeSymbol.O_SYMBOL);

    fun nextTurn() = if (this == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER
}

enum class TicTacToeSymbol(val cellResId: Int) {
    EMPTY(R.drawable.empty_cell_background), X_SYMBOL(R.drawable.x_cell_background), O_SYMBOL(R.drawable.o_cell_background);
}

enum class GameStates {
    PLAY, FINISHED
}

data class Cell(val row: Int, val column: Int, var cellState: TicTacToeSymbol = TicTacToeSymbol.EMPTY)

class TicTacToeGameController @Inject constructor() {

    private var state: GameStates = GameStates.PLAY
    var currentTurn = Turn.FIRST_PLAYER

    lateinit var gameTable: TicTacToeTable
    lateinit var gameResultPublisher: TicTacToeResultPublisher

    fun onCellClicked(cell: Cell) {
        if (state == GameStates.PLAY)
            if (cell.cellState == TicTacToeSymbol.EMPTY) {
                cell.cellState = currentTurn.cellState
                currentTurn = currentTurn.nextTurn()

                verifyEndGame()
            }
    }

    private fun List<Cell>.getWinnerSymbol(): TicTacToeSymbol {
        val firstCellState = this[0].cellState

        return if (firstCellState == this[1].cellState && firstCellState == this[2].cellState) {
            firstCellState
        } else {
            TicTacToeSymbol.EMPTY
        }
    }

    private fun verifyEndGame() {
        var winner = gameTable.firsRow.getWinnerSymbol()

        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.secondRow.getWinnerSymbol()
        }
        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.thirdRow.getWinnerSymbol()
        }

        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.firsColumn.getWinnerSymbol()
        }
        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.secondColumn.getWinnerSymbol()
        }
        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.thirdColumn.getWinnerSymbol()
        }

        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.firstDiagonal.getWinnerSymbol()
        }
        if (winner == TicTacToeSymbol.EMPTY) {
            winner = gameTable.secondDiagonal.getWinnerSymbol()
        }

        if (winner != TicTacToeSymbol.EMPTY) {
            state = GameStates.FINISHED
            gameResultPublisher.publish(winner)
        }
    }

    fun configure(ticTacToeResultPublisher: TicTacToeResultPublisher, gameTable: TicTacToeTable) {
        this.gameResultPublisher = ticTacToeResultPublisher
        this.gameTable = gameTable
    }

    fun reset() {
        gameTable.reset()
        state = GameStates.PLAY
        gameResultPublisher.reset()
    }

}