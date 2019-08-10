package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.TicTacToeCell
import com.elihimas.games.pastimes.game.Score
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.game.TicTacToeResultPublisher
import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.model.TicTacToeTable

class TicTacToeGameViewModel : ViewModel(), TicTacToeResultPublisher {

    private val game = TicTacToeGameController()

    val score = MutableLiveData<Score>()
    val ticTacToeTableData = MutableLiveData<TicTacToeTable>()
    val changedCell = MutableLiveData<TicTacToeCell>()
    val instructionResId = MutableLiveData<Int>()

    init {
        val ticTacToeTable = TicTacToeTable()
        ticTacToeTableData.value = ticTacToeTable

        game.configure(this, ticTacToeTable)
    }

    fun onCellClicked(cell: TicTacToeCell) {
        game.onCellClicked(cell)
    }

    fun setGameMode(gameMode: GameMode) {
        game.setGameMode(gameMode)
    }

    override fun publishScore(score: Score) {
        this.score.value = score
    }

    override fun publishCellUpdate(changedCell: TicTacToeCell) {
        this.changedCell.value = changedCell
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
