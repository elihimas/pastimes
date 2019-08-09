package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.model.Cell
import com.elihimas.games.pastimes.model.TicTacToeTable

class TicTacToeGameViewModel : ViewModel() {

    val ticTacToeTable = MutableLiveData<TicTacToeTable>().apply {
        value = TicTacToeTable()
    }

    val changedCell = MutableLiveData<Cell>()

    fun onCellClicked(cell: Cell) {
        ticTacToeTable.value?.onCellClicked(cell)

        changedCell.value = cell
    }

}
