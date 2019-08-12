package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.model.Suggestion
import com.elihimas.games.pastimes.model.TicTacToeCell

interface TicTacToeResultPublisher {
    fun publishScore(score: Score)
    fun publishCellUpdate(changedCell: TicTacToeCell)
    fun publishReset()
    fun publishInstruction(instructionStringResId: Int)
    fun publishResult(gameResult: GameResult)
    fun publishSuggestion(suggestion: Suggestion)
}
