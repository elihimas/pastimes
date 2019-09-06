package com.elihimas.games.pastimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.GameResult
import com.elihimas.games.pastimes.game.Score
import com.elihimas.games.pastimes.game.TicTacToeGameBridge
import com.elihimas.games.pastimes.game.TicTacToeResultPublisher
import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.model.Suggestion
import com.elihimas.games.pastimes.model.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

class TicTacToeGameViewModel : ViewModel(), TicTacToeResultPublisher {

    private val game: TicTacToeGameBridge

    val score = MutableLiveData<Score>()
    val ticTacToeTableData = MutableLiveData<TicTacToeTable>()
    val changedCell = MutableLiveData<TicTacToeCell>()
    val instructionResId = MutableLiveData<Int>()
    val result = MutableLiveData<GameResult>()
    val suggestion = MutableLiveData<Suggestion>()

    @Inject
    lateinit var preferences: PastimesPreferences

    init {
        PastimesApplication.appComponent.inject(this)

        val ticTacToeTable = TicTacToeTable()
        ticTacToeTableData.value = ticTacToeTable

        val gameMode = preferences.getMode()
        game = TicTacToeGameBridge(this, ticTacToeTable, gameMode)
    }

    fun onCellClicked(cell: TicTacToeCell) {
        game.onCellClicked(cell)
    }

    fun changeGameMode(gameMode: GameMode) {
        game.changeGameMode(gameMode)
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

    override fun publishResult(gameResult: GameResult) {
        result.value = gameResult
    }

    fun reset() {
        game.reset()
    }

    override fun publishSuggestion(suggestion: Suggestion) {
        this.suggestion.value = suggestion
    }

    fun clearScore() {
        preferences.resetVictoryCount()
        publishScore(Score())
    }
}
