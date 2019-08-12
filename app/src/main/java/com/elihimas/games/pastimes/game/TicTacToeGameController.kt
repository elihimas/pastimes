package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.*

data class Score(val xVictoryCount: Int, val oVictoryCount: Int)

data class GameResult(val cells: List<TicTacToeCell>?, val winnerSymbol: TicTacToeSymbol)

class TicTacToeGameController(
    ticTacToeResultPublisher: TicTacToeResultPublisher,
    gameTable: TicTacToeTable,
    gameMode: GameMode
) :
    AbstractTicTacToeGameController(ticTacToeResultPublisher, gameTable,gameMode) {

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }

    override fun onCellClicked(cell: TicTacToeCell) {
        doOnCellClicked(cell, false)
    }

    private fun doOnCellClicked(cell: TicTacToeCell, isIAAction: Boolean) {
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

            publisher.publishCellUpdate(cell)
            publisher.publishInstruction(nextInstruction)

            if (!isIAAction) {
                performAIActionIfNecessary()
            }
        }
    }

    private fun performAIActionIfNecessary() {
        if (state == GameStates.PLAY && gameMode.isVersusAI()) {
            val cell = ia.pickCell(playerInTurn, currentTurn)

            if (cell != null && cell.isFree()) {
                doOnCellClicked(cell, true)
            }
        }
    }


    override fun changeGameMode(gameMode: GameMode) {
        super.changeGameMode(gameMode)

        if (this.gameMode != gameMode) {
            publisher.publishInstruction(R.string.instruction_game_start)
            ia.configure(gameTable, gameMode)

            this.gameMode = gameMode
            ia.configure(gameTable, gameMode)

            reset()
        }
    }

}

