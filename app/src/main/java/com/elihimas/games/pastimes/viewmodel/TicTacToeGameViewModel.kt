package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.CellData
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.game.TicTacToeResultPublisher
import com.elihimas.games.pastimes.model.TicTacToeTable

class TicTacToeGameViewModel : ViewModel(), TicTacToeResultPublisher {

    private val game = TicTacToeGameController()

    val ticTacToeTableData = MutableLiveData<TicTacToeTable>()
    val changedCell = MutableLiveData<CellData>()
    val instructionResId = MutableLiveData<Int>()

    init {
        val ticTacToeTable = TicTacToeTable()
        ticTacToeTableData.value = ticTacToeTable

        game.configure(this, ticTacToeTable)
    }

    fun onCellClicked(cellData: CellData) {
        game.onCellClicked(cellData)
    }

    override fun publishCellUpdate(cellData: CellData) {
        changedCell.value = cellData
    }

    override fun publishReset() {
        instructionResId.value = R.string.instruction_game_start
        ticTacToeTableData.value = ticTacToeTableData.value
    }

    override fun publishInstruction(instructionStringResId: Int) {
        instructionResId.value = instructionStringResId
    }

    fun reset() {
        game.reset()
    }
}
