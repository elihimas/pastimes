package com.elihimas.games.pastimes.extensions

import com.elihimas.games.pastimes.game.TicTacToeCell

const val ONE_SIXTH = 1f / 6
const val ONE_THIRD = 1f / 3

fun List<TicTacToeCell>.getStartX(): Float {
    val first = first()
    val last = last()

    return if (first.column == last.column) {
        (ONE_SIXTH + first.column * ONE_THIRD)
    } else {
        0f
    }
}

fun List<TicTacToeCell>.getStartY(): Float {
    val first = first()
    val last = last()
    val isSameRow = first.row == last.row

    return if (isSameRow) {
        (ONE_SIXTH + first.row * ONE_THIRD)
    } else {
        val isUpDiagonal = last.row == 0

        if (isUpDiagonal) {
            1f
        } else {
            0f
        }
    }
}

fun List<TicTacToeCell>.getEndX(): Float {
    val first = first()
    val last = last()
    val isSameColumn = first.column == last.column

    return if (isSameColumn) {
        (ONE_SIXTH + first.column * ONE_THIRD)
    } else {
        1f
    }
}

fun List<TicTacToeCell>.getEndY(): Float {
    val first = first()
    val last = last()
    val inSameRow = first.row == last.row

    return if (inSameRow) {
        ONE_SIXTH + first.row * ONE_THIRD
    } else {
        val isUpDiagonal = last.row == 0

        if (isUpDiagonal) {
            0f
        } else {
            1f
        }
    }
}
