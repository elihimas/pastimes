package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.game.CellData
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.game.TicTacToeResultPublisher
import com.elihimas.games.pastimes.game.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable

class TicTacToeGameViewModel : ViewModel(), TicTacToeResultPublisher {

    val game = TicTacToeGameController()

    val ticTacToeTableData = MutableLiveData<TicTacToeTable>()
    val changedCell = MutableLiveData<CellData>()
    val winnerSymbol = MutableLiveData<TicTacToeSymbol>()

    init {
        val ticTacToeTable = TicTacToeTable()
        ticTacToeTableData.value = ticTacToeTable

        game.configure(this, ticTacToeTable)
    }

    fun onCellClicked(cellData: CellData) {
        game.onCellClicked(cellData)
    }

    override fun publishVictory(winner: TicTacToeSymbol) {
        winnerSymbol.value = winner
    }

    override fun publishCellUpdate(cellData: CellData) {
        changedCell.value = cellData
    }

    override fun publishReset() {
        winnerSymbol.value = TicTacToeSymbol.EMPTY
        ticTacToeTableData.value = ticTacToeTableData.value
    }

    fun reset() {
        game.reset()
    }
}
