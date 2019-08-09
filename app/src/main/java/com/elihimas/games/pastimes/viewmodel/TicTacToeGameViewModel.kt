package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.model.Cell
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

class TicTacToeGameViewModel : ViewModel() {

    @Inject
    lateinit var game: TicTacToeGameController

    val ticTacToeTable = MutableLiveData<TicTacToeTable>().apply {
        value = TicTacToeTable()
    }

    val changedCell = MutableLiveData<Cell>()

    init {
        PastimesApplication.appComponent.inject(this)
    }

    fun onCellClicked(cell: Cell) {
        game.onCellClicked(cell)
        changedCell.value = cell
    }

}
