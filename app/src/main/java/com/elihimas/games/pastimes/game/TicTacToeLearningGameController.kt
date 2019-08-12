package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.extensions.almostVictoryOf
import com.elihimas.games.pastimes.extensions.filterFreeCells
import com.elihimas.games.pastimes.model.*


class TicTacToeLearningGameController(
    publisher: TicTacToeResultPublisher,
    gameTable: TicTacToeTable,
    gameMode: GameMode
) :
    AbstractTicTacToeGameController(publisher, gameTable, gameMode) {

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }

    init {
        ia.configure(gameTable, GameMode.EASY)

        performAIAction()
    }

    override fun onCellClicked(cell: TicTacToeCell) {
        val suggestion = evaluatePick(cell)

        if (suggestion.isSuggestionNeeded()) {
            publisher.publishSuggestion(suggestion)
        } else {
            doOnCellClicked(cell)
        }
    }

    private fun evaluatePick(cell: TicTacToeCell) =
        run {
            val victoryCells =
                gameTable.allLines.filter { line -> line.almostVictoryOf(playerInTurn.symbol) }.filterFreeCells()
                    .toList()

            victoryCells.let { cells ->
                if (victoryCells.isEmpty()) {
                    null
                } else if (!cells.contains(cell)) {
                    //TODO make the callback be called
                    val suggestionShownCallback = {
                        doOnCellClicked(cell)
                    }

                    Suggestion(
                        SuggestionType.MISSED_A_VICTORY,
                        cell,
                        R.string.suggestion_missed_victory,
                        suggestionShownCallback,
                        victoryCells
                    )
                } else {
                    Suggestion(SuggestionType.NOTHING_TO_SAY, cell)
                }
            }
        } ?: run {
            val defeatCells =
                gameTable.allLines.filter { line -> line.almostVictoryOf(playerInTurn.nextTurn().symbol) }
                    .filterFreeCells()
                    .toList()

            defeatCells.let { cells ->
                if (defeatCells.isNotEmpty() && !cells.contains(cell)) {
                    //TODO make the callback be called
                    val suggestionShownCallback = {
                        doOnCellClicked(cell)
                    }

                    Suggestion(
                        SuggestionType.MISSED_A_DEFEAT,
                        cell,
                        R.string.suggestion_missed_defeat,
                        suggestionShownCallback,
                        defeatCells
                    )

                } else {
                    null
                }
            }
        }
        ?: Suggestion(SuggestionType.NOTHING_TO_SAY, cell)

    private fun doOnCellClicked(cell: TicTacToeCell, isIAAction: Boolean = false) {
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
                performAIAction()
            }
        }
    }

    private fun performAIAction() {
        val pickedCell = ia.pickCell(playerInTurn, currentTurn)
        pickedCell?.let { cell ->
            doOnCellClicked(cell, true)
        }
    }

    override fun reset() {
        super.reset()

        performAIAction()
    }

}
