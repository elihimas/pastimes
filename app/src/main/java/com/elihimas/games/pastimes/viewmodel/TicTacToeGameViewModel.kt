package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.game.Cell
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.game.TicTacToeResultPublisher
import com.elihimas.games.pastimes.game.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable
import javax.inject.Inject

class TicTacToeGameViewModel : ViewModel(), TicTacToeResultPublisher {

    val game = TicTacToeGameController()

    val ticTacToeTableData = MutableLiveData<TicTacToeTable>()
    val changedCell = MutableLiveData<Cell>()
    val winnerSymbol = MutableLiveData<TicTacToeSymbol>()

    init {
        val ticTacToeTable = TicTacToeTable()
        ticTacToeTableData.value = ticTacToeTable

        game.configure(this, ticTacToeTable)
    }

    fun onCellClicked(cell: Cell) {
        game.onCellClicked(cell)
        changedCell.value = cell
    }

    override fun publishVictory(winner: TicTacToeSymbol) {
        winnerSymbol.value = winner
    }

    override fun publishReset() {
        winnerSymbol.value = TicTacToeSymbol.EMPTY
        ticTacToeTableData.value = ticTacToeTableData.value
    }

    fun reset() {
        game.reset()
    }
}
