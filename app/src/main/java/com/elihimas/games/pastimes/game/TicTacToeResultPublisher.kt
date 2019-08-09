package com.elihimas.games.pastimes.game


interface TicTacToeResultPublisher {
    fun publishScore(score:Score)
    fun publishCellUpdate(cellData: CellData)
    fun publishReset()
    fun publishInstruction(instructionStringResId: Int)
}
