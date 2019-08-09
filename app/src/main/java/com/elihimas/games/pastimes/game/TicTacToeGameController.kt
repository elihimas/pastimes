package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.TicTacToeTable


enum class Turn(val cellState: TicTacToeSymbol) {
    FIRST_PLAYER(TicTacToeSymbol.X_SYMBOL), SECOND_PLAYER(TicTacToeSymbol.O_SYMBOL);

    fun nextTurn() = if (this == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER
}

enum class TicTacToeSymbol {
    EMPTY, X_SYMBOL, O_SYMBOL
}

enum class GameStates {
    PLAY, FINISHED
}

data class CellData(val row: Int, val column: Int, var cellSymbol: TicTacToeSymbol = TicTacToeSymbol.EMPTY)

class TicTacToeGameController {

    private var state: GameStates = GameStates.PLAY
    var currentTurn = Turn.FIRST_PLAYER

    lateinit var gameTable: TicTacToeTable
    lateinit var gameResultPublisher: TicTacToeResultPublisher

    fun onCellClicked(cellData: CellData) {
        if (state == GameStates.PLAY)
            if (cellData.cellSymbol == TicTacToeSymbol.EMPTY) {
                cellData.cellSymbol = currentTurn.cellState
                currentTurn = currentTurn.nextTurn()

                verifyEndGame()
                gameResultPublisher.publishCellUpdate(cellData)
            }
    }

    private fun List<CellData>.getWinnerSymbol(): TicTacToeSymbol {
        val firstCellState = this[0].cellSymbol

        return if (firstCellState == this[1].cellSymbol && firstCellState == this[2].cellSymbol) {
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
            gameResultPublisher.publishVictory(winner)
        }
    }

    fun configure(ticTacToeResultPublisher: TicTacToeResultPublisher, gameTable: TicTacToeTable) {
        this.gameResultPublisher = ticTacToeResultPublisher
        this.gameTable = gameTable
    }

    fun reset() {
        gameTable.reset()
        state = GameStates.PLAY
        gameResultPublisher.publishReset()
    }

}