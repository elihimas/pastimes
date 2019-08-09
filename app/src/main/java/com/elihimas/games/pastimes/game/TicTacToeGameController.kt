package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject


enum class Turn(val cellState: TicTacToeSymbol, val instructionStringResId: Int) {
    FIRST_PLAYER(TicTacToeSymbol.X_SYMBOL, R.string.instruction_x_turn),
    SECOND_PLAYER(TicTacToeSymbol.O_SYMBOL, R.string.instruction_o_turn);

    fun nextTurn() = if (this == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER
}

enum class TicTacToeSymbol(val resultMessageResId: Int) {
    EMPTY(R.string.result_message_draw), X_SYMBOL(R.string.result_x_victory), O_SYMBOL(R.string.result_o_victory)
}

enum class GameStates {
    PLAY, FINISHED
}

data class CellData(val row: Int, val column: Int, var cellSymbol: TicTacToeSymbol = TicTacToeSymbol.EMPTY)

data class Score(val xVictoryCount: Int, val oVictoryCount: Int)

class TicTacToeGameController {

    @Inject
    lateinit var preferences: PastimesPreferences

    private var state: GameStates = GameStates.PLAY
    private var currentTurn = Turn.FIRST_PLAYER

    private lateinit var gameTable: TicTacToeTable
    private lateinit var gameStatePublisher: TicTacToeResultPublisher

    init {
        PastimesApplication.appComponent.inject(this)
    }

    fun onCellClicked(cellData: CellData) {
        if (state == GameStates.PLAY)
            if (cellData.cellSymbol == TicTacToeSymbol.EMPTY) {
                cellData.cellSymbol = currentTurn.cellState

                val winnerSymbol = verifyGameVictory()
                var nextInstruction: Int

                if (winnerSymbol == null) {
                    currentTurn = currentTurn.nextTurn()
                    nextInstruction = currentTurn.instructionStringResId
                } else {
                    var xVictoryCount = preferences.getXVictoryCount()
                    var oVictoryCount = preferences.getOVictoryCount()

                    if (winnerSymbol == TicTacToeSymbol.X_SYMBOL) {
                        xVictoryCount++
                        preferences.setXVictoryCount(xVictoryCount)
                    } else if (winnerSymbol == TicTacToeSymbol.O_SYMBOL) {
                        oVictoryCount++
                        preferences.setOVictoryCount(oVictoryCount)
                    }

                    gameStatePublisher.publishScore(Score(xVictoryCount, oVictoryCount))

                    nextInstruction = winnerSymbol.resultMessageResId
                }
                gameStatePublisher.publishCellUpdate(cellData)
                gameStatePublisher.publishInstruction(nextInstruction)
            }
    }

    private fun List<CellData>.verifyVictoryAndReturnWinnerSymbol(): TicTacToeSymbol? {
        val firstCellState = this[0].cellSymbol
        var hasVictory =
            firstCellState != TicTacToeSymbol.EMPTY
                    && firstCellState == this[1].cellSymbol
                    && firstCellState == this[2].cellSymbol

        return if (hasVictory) {
            firstCellState
        } else {
            null
        }
    }

    //TODO refactor verification
    //TODO return TicTacToeSymbol.EMPTY if draw
    private fun verifyGameVictory(): TicTacToeSymbol? {
        var winnerSymbol = gameTable.firsRow.verifyVictoryAndReturnWinnerSymbol()

        if (winnerSymbol == null) {
            winnerSymbol = gameTable.secondRow.verifyVictoryAndReturnWinnerSymbol()
        }
        if (winnerSymbol == null) {
            winnerSymbol = gameTable.thirdRow.verifyVictoryAndReturnWinnerSymbol()
        }

        if (winnerSymbol == null) {
            winnerSymbol = gameTable.firsColumn.verifyVictoryAndReturnWinnerSymbol()
        }
        if (winnerSymbol == null) {
            winnerSymbol = gameTable.secondColumn.verifyVictoryAndReturnWinnerSymbol()
        }
        if (winnerSymbol == null) {
            winnerSymbol = gameTable.thirdColumn.verifyVictoryAndReturnWinnerSymbol()
        }

        if (winnerSymbol == null) {
            winnerSymbol = gameTable.firstDiagonal.verifyVictoryAndReturnWinnerSymbol()
        }
        if (winnerSymbol == null) {
            winnerSymbol = gameTable.secondDiagonal.verifyVictoryAndReturnWinnerSymbol()
        }

        if (winnerSymbol != null) {
            state = GameStates.FINISHED
        }

        return winnerSymbol
    }

    fun configure(ticTacToeResultPublisher: TicTacToeResultPublisher, gameTable: TicTacToeTable) {
        this.gameTable = gameTable
        gameStatePublisher = ticTacToeResultPublisher

        gameStatePublisher.publishInstruction(R.string.instruction_game_start)

        val xVictoryCount = preferences.getXVictoryCount()
        val oVictoryCount = preferences.getOVictoryCount()

        gameStatePublisher.publishScore(Score(xVictoryCount, oVictoryCount))
    }

    fun reset() {
        gameTable.reset()
        state = GameStates.PLAY
        gameStatePublisher.publishReset()
    }

}