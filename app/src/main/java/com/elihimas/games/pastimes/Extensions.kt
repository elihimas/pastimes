package com.elihimas.games.pastimes.extensions

import com.elihimas.games.pastimes.game.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeSymbol

const val ONE_SIXTH = 1f / 6
const val ONE_THIRD = 1f / 3

fun List<TicTacToeCell>.isAllFree() = all { cell -> cell.isFree() }

fun TicTacToeCell.isInFreeColumn() =
    when (column) {
        0 -> table.firstColumn
        1 -> table.secondColumn
        2 -> table.thirdColumn
        else -> throw IllegalStateException("not implemented for $column")
    }.isAllFree()

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

fun <E> List<E>.pickRandom(): E = this[(Math.random() * this.size).toInt()]

fun List<TicTacToeCell>.filterFreeCells() = filter { cell -> cell.isFree() }

//TODO revisit this
fun List<List<TicTacToeCell>>.filterFreeCells() =
    sequence {
        forEach { line ->
            yieldAll(line.filterFreeCells())
        }
    }


fun List<TicTacToeCell>.almostVictory() = count { cell -> cell.cellSymbol == TicTacToeSymbol.O_SYMBOL } == 2
        || count { cell -> cell.cellSymbol == TicTacToeSymbol.X_SYMBOL } == 2

fun List<TicTacToeCell>.almostVictoryOf(symbol: TicTacToeSymbol) =
    count { cell -> cell.cellSymbol == symbol } == 2 && count { cell -> cell.cellSymbol == TicTacToeSymbol.NONE } == 1

fun List<TicTacToeCell>.hasAPickBy(symbol: TicTacToeSymbol) = this.any { cell -> cell.cellSymbol == symbol }

fun List<TicTacToeCell>.hasTwoPicksBy(symbol: TicTacToeSymbol) = this.count { cell -> cell.cellSymbol == symbol } == 2

fun listFreeInBothDirectionsByRow(row: List<TicTacToeCell>) =
    sequence {
        if (row.isAllFree()) {
            row.forEach { cell ->
                if (cell.isInFreeColumn()) {
                    yield(cell)
                }
            }
        }
    }