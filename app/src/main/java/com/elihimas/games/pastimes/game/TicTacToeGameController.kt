package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.*
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import java.lang.IllegalStateException
import javax.inject.Inject

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

data class Score(val xVictoryCount: Int, val oVictoryCount: Int)


data class GameResult(val cells: List<TicTacToeCell>?, val winnerSymbol: TicTacToeSymbol)

class TicTacToeGameController {

    private companion object {
        const val LAST_POSSIBLE_TURN = 9
    }

    @Inject
    lateinit var preferences: PastimesPreferences

    private var state: GameStates = GameStates.PLAY
    private var playerInTurn = PlayerInTurn.X_PLAYER
    private var currentTurn = 0
    private var gameMode: GameMode
    private lateinit var gameTable: TicTacToeTable

    private var ia = TicTacToeAI()

    private lateinit var gameStatePublisher: TicTacToeResultPublisher

    init {
        PastimesApplication.appComponent.inject(this)

        gameMode = preferences.getMode()
    }

    fun onCellClicked(cell: TicTacToeCell, isIAAction: Boolean = false) {
        if (state == GameStates.PLAY && cell.isFree()) {
            cell.cellSymbol = playerInTurn.symbol
            currentTurn++

            val gameResult = verifyResult()
            var nextInstruction: Int

            if (gameResult == null) {
                playerInTurn = playerInTurn.nextTurn()
                nextInstruction = playerInTurn.instructionStringResId
            } else {
                processGameResult(gameResult)

                nextInstruction = gameResult.winnerSymbol.resultMessageResId
            }

            gameStatePublisher.publishCellUpdate(cell)
            gameStatePublisher.publishInstruction(nextInstruction)

            if (!isIAAction) {
                performAIActionIfNecessary()
            }
        }
    }

    private fun processGameResult(gameResult: GameResult) {
        var xVictoryCount = preferences.getXVictoryCount()
        var oVictoryCount = preferences.getOVictoryCount()

        if (gameResult.winnerSymbol == TicTacToeSymbol.X_SYMBOL) {
            xVictoryCount++
            preferences.setXVictoryCount(xVictoryCount)
        } else if (gameResult.winnerSymbol == TicTacToeSymbol.O_SYMBOL) {
            oVictoryCount++
            preferences.setOVictoryCount(oVictoryCount)
        }

        gameStatePublisher.publishScore(Score(xVictoryCount, oVictoryCount))
        gameStatePublisher.publishResult(gameResult)
    }

    private fun performAIActionIfNecessary() {
        if (state == GameStates.PLAY && gameMode.isVersusAI()) {
            val cell = ia.pickCell(playerInTurn, currentTurn)

            if (cell != null && cell.isFree()) {
                onCellClicked(cell, true)
            }
        }
    }

    private fun List<TicTacToeCell>.verifyResult(): GameResult? {
        val firstCellSymbol = first().cellSymbol
        var hasVictory =
            firstCellSymbol != TicTacToeSymbol.NONE
                    && firstCellSymbol == this[1].cellSymbol
                    && firstCellSymbol == this[2].cellSymbol

        return if (hasVictory) {
            GameResult(this, firstCellSymbol)
        } else {
            null
        }
    }

    private fun verifyResult(): GameResult? {
        var winResult =
            gameTable.firstRow.verifyResult()
                ?: gameTable.secondRow.verifyResult()
                ?: gameTable.thirdRow.verifyResult()
                ?: gameTable.firstColumn.verifyResult()
                ?: gameTable.secondColumn.verifyResult()
                ?: gameTable.thirdColumn.verifyResult()
                ?: gameTable.firstDiagonal.verifyResult()
                ?: gameTable.secondDiagonal.verifyResult()
                ?: if (currentTurn == LAST_POSSIBLE_TURN) {
                    GameResult(null, TicTacToeSymbol.NONE)
                } else {
                    null
                }

        if (winResult != null) {
            state = GameStates.FINISHED
        }

        return winResult
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
        currentTurn = 0
        gameTable.reset()
        state = GameStates.PLAY
        playerInTurn = PlayerInTurn.X_PLAYER
        gameMode = preferences.getMode()
        gameStatePublisher.publishReset()
    }

    fun setGameMode(gameMode: GameMode) {
        if (this.gameMode != gameMode) {
            this.gameMode = gameMode
            preferences.setMode(gameMode)
            ia.configure(gameTable, gameMode)

            reset()
        }
    }

}

