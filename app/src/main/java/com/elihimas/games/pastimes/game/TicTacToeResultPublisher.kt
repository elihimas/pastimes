package com.elihimas.games.pastimes.game


interface TicTacToeResultPublisher {
    fun publish(winner: TicTacToeSymbol)
    fun reset()
}
