package com.elihimas.games.pastimes.game


interface TicTacToeResultPublisher {
    fun publishScore(score: Score)
    fun publishCellUpdate(cell: TicTacToeCell)
    fun publishReset()
    fun publishInstruction(instructionStringResId: Int)
    fun publishResult(gameResult: GameResult)
}
