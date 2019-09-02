package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.extensions.verifyResult
import com.elihimas.games.pastimes.model.*
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

abstract class AbstractTicTacToeGameController(
    val publisher: TicTacToeResultPublisher,
    val gameTable: TicTacToeTable,
    var gameMode: GameMode
) {

    private companion object {
        const val LAST_POSSIBLE_TURN = 9
    }

    lateinit var playerInTurn: PlayerInTurn
    lateinit var state: GameStates

    val ia = TicTacToeAI()
    var currentTurn = 0

    @Inject
    lateinit var preferences: PastimesPreferences

    init {
        injectDagger()

        val xVictoryCount = preferences.getXVictoryCount()
        val oVictoryCount = preferences.getOVictoryCount()

        publisher.publishScore(Score(xVictoryCount, oVictoryCount))

        reset()
    }

    abstract fun injectDagger()

    abstract fun onCellClicked(cell: TicTacToeCell)
    open fun changeGameMode(gameMode: GameMode) {
        preferences.setMode(gameMode)
    }

    open fun reset() {
        state = GameStates.PLAY
        playerInTurn = PlayerInTurn.getFirstTurn()
        currentTurn = 0
        gameMode = preferences.getMode()
        gameTable.reset()

        publisher.publishReset()
    }

    fun verifyResult(): GameResult? {
        val winResult =
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

    fun processGameResult(gameResult: GameResult) {
        var xVictoryCount = preferences.getXVictoryCount()
        var oVictoryCount = preferences.getOVictoryCount()

        if (gameResult.winnerSymbol == TicTacToeSymbol.X_SYMBOL) {
            xVictoryCount++
            preferences.setXVictoryCount(xVictoryCount)
        } else if (gameResult.winnerSymbol == TicTacToeSymbol.O_SYMBOL) {
            oVictoryCount++
            preferences.setOVictoryCount(oVictoryCount)
        }

        publisher.publishScore(Score(xVictoryCount, oVictoryCount))
        publisher.publishResult(gameResult)
    }
}