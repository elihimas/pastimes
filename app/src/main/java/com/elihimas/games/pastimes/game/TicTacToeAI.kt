package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.model.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable

private interface IA {
    fun defineCell(): TicTacToeCell?
}

private class EasyIA(val gameTable: TicTacToeTable) : IA {
    override fun defineCell(): TicTacToeCell? {
        var chosenCell: TicTacToeCell? = null
        for (row in 0..2) {
            for (column in 0..2) {
                val currentCell = gameTable.cells[row][column]
                if (currentCell.cellSymbol == TicTacToeSymbol.EMPTY) {
                    chosenCell = currentCell

                    break
                }
            }

            if (chosenCell != null) {
                break
            }
        }

        return chosenCell
    }

}

class TicTacToeAI {

    private lateinit var gameTable: TicTacToeTable
    private lateinit var gameMode: GameMode
    private var ia: IA? = null

    fun configure(gameTable: TicTacToeTable, gameMode: GameMode) {
        this.gameTable = gameTable
        this.gameMode = gameMode

        ia = when (gameMode) {
            GameMode.EASY -> EasyIA(gameTable)
            GameMode.MEDIUM -> EasyIA(gameTable)
            GameMode.IMPOSSIBLE -> EasyIA(gameTable)
            else -> null
        }
    }

    fun defineCell() = ia?.defineCell()

}