package com.elihimas.games.pastimes.model

import com.elihimas.games.pastimes.R
import java.lang.IllegalStateException

//TODO remove this enum and use only TicTacToeSymbol
enum class PlayerInTurn(val symbol: TicTacToeSymbol, val instructionStringResId: Int) {
    X_PLAYER(TicTacToeSymbol.X_SYMBOL, R.string.instruction_x_turn),
    O_PLAYER(TicTacToeSymbol.O_SYMBOL, R.string.instruction_o_turn);

    fun nextTurn() = if (this == X_PLAYER) O_PLAYER else X_PLAYER
}

enum class TicTacToeSymbol(val resultMessageResId: Int) {
    NONE(R.string.result_message_draw), X_SYMBOL(R.string.result_x_victory), O_SYMBOL(R.string.result_o_victory)
}

enum class GameStates { PLAY, FINISHED }

enum class GameMode(val id: Int) {
    EASY(1), MEDIUM(2), IMPOSSIBLE(3), OTHER_PLAYER(4);

    fun isVersusAI() = this != OTHER_PLAYER

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
