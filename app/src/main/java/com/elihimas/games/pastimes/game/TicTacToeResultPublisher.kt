package com.elihimas.games.pastimes.game


interface TicTacToeResultPublisher {
    fun publishCellUpdate(cellData: CellData)
    fun publishReset()
    fun publishInstruction(instructionStringResId: Int)
}
