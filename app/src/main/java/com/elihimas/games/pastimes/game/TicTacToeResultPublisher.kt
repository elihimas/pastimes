package com.elihimas.games.pastimes.game


interface TicTacToeResultPublisher {
    fun publishVictory(winner: TicTacToeSymbol)
    fun publishReset()
}
