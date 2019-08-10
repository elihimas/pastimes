package com.elihimas.games.pastimes.model

import com.elihimas.games.pastimes.R
import java.lang.IllegalStateException


enum class Turn(val cellState: TicTacToeSymbol, val instructionStringResId: Int) {
    FIRST_PLAYER(TicTacToeSymbol.X_SYMBOL, R.string.instruction_x_turn),
    SECOND_PLAYER(TicTacToeSymbol.O_SYMBOL, R.string.instruction_o_turn);

    fun nextTurn() = if (this == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER
}

enum class TicTacToeSymbol(val resultMessageResId: Int) {
    EMPTY(R.string.result_message_draw), X_SYMBOL(R.string.result_x_victory), O_SYMBOL(R.string.result_o_victory)
}

enum class GameStates { PLAY, FINISHED }

enum class GameMode(val id: Int) {
    EASY(1), MEDIUM(2), IMPOSSIBLE(3), OTHER_PLAYER(4);

    companion object {
        fun findModeById(id: Int) = when (id) {
            EASY.id -> EASY
            MEDIUM.id -> MEDIUM
            IMPOSSIBLE.id -> IMPOSSIBLE
            OTHER_PLAYER.id -> OTHER_PLAYER
            else -> throw IllegalStateException("not implemented for $id")
        }
    }
}
