package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.*
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

data class TicTacToeCell(val row: Int, val column: Int, var cellSymbol: TicTacToeSymbol = TicTacToeSymbol.EMPTY)

data class Score(val xVictoryCount: Int, val oVictoryCount: Int)

class TicTacToeGameController {

    @Inject
    lateinit var preferences: PastimesPreferences

    private var state: GameStates = GameStates.PLAY
    private var currentTurn = Turn.FIRST_PLAYER
    private var gameMode: GameMode
    private lateinit var gameTable: TicTacToeTable

    private var ia = TicTacToeAI()

    private lateinit var gameStatePublisher: TicTacToeResultPublisher

    init {
        PastimesApplication.appComponent.inject(this)

        gameMode = preferences.getMode()
    }

    fun onCellClicked(cell: TicTacToeCell, isIAAction: Boolean = false) {
        if (state == GameStates.PLAY && cell.cellSymbol == TicTacToeSymbol.EMPTY) {
            cell.cellSymbol = currentTurn.cellState

            val winnerSymbol = verifyGameVictoryAndGetWinnerSymbol()
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

            gameStatePublisher.publishCellUpdate(cell)
            gameStatePublisher.publishInstruction(nextInstruction)

            if (!isIAAction) {
                performAIActionIfNecessary()
            }
        }
    }

    private fun performAIActionIfNecessary() {
        if (gameMode != GameMode.OTHER_PLAYER) {
            val cell = ia.defineCell()

            if (cell != null && cell.cellSymbol == TicTacToeSymbol.EMPTY) {
                onCellClicked(cell, true)
            }
        }
    }

    private fun List<TicTacToeCell>.verifyVictoryAndReturnWinnerSymbol(): TicTacToeSymbol? {
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
    private fun verifyGameVictoryAndGetWinnerSymbol(): TicTacToeSymbol? {
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
        ia.configure(gameTable, gameMode)

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

    fun setGameMode(gameMode: GameMode) {
        if (this.gameMode != gameMode) {
            this.gameMode = gameMode
            preferences.setMode(gameMode)

            reset()
        }
    }

}