package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.model.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeTable

class TicTacToeGameBridge(
    private val ticTacToeResultPublisher: TicTacToeResultPublisher,
    private val gameTable: TicTacToeTable,
    private var gameMode: GameMode
) {

    private var controller: AbstractTicTacToeGameController

    init {
        controller = if (gameMode.isLearning()) {
            TicTacToeLearningGameController(ticTacToeResultPublisher, gameTable, gameMode)
        } else {
            TicTacToeGameController(ticTacToeResultPublisher, gameTable, gameMode)
        }
        controller.changeGameMode(gameMode)
    }

    fun onCellClicked(cell: TicTacToeCell) {
        controller.onCellClicked(cell)
    }

    fun changeGameMode(gameMode: GameMode) {
        this.gameMode = gameMode
        if (gameMode == GameMode.LEARNING) {
            if (controller !is TicTacToeLearningGameController) {
                controller = TicTacToeLearningGameController(ticTacToeResultPublisher, gameTable, gameMode)
            }
        } else {
            if (controller !is TicTacToeGameController) {
                controller = TicTacToeGameController(ticTacToeResultPublisher, gameTable, gameMode)
            }
        }

        controller.changeGameMode(gameMode)
    }

    fun reset() {
        controller.reset()
    }

}