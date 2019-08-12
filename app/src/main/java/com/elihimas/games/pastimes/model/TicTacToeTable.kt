package com.elihimas.games.pastimes.model

import com.elihimas.games.pastimes.extensions.almostVictoryOf
import com.elihimas.games.pastimes.extensions.filterFreeCells
import com.elihimas.games.pastimes.extensions.isAllFree
import com.elihimas.games.pastimes.extensions.listFreeInBothDirectionsByRow

class TicTacToeTable {

    val cells = Array(3) { row ->
        Array(3) { column ->
            TicTacToeCell(row, column, this)
        }
    }

    val firstRow by lazy {
        listOf(cells[0][0], cells[0][1], cells[0][2])
    }

    val secondRow by lazy {
        listOf(cells[1][0], cells[1][1], cells[1][2])
    }

    val thirdRow by lazy {
        listOf(cells[2][0], cells[2][1], cells[2][2])
    }

    val firstColumn by lazy {
        listOf(cells[0][0], cells[1][0], cells[2][0])
    }

    val secondColumn by lazy {
        listOf(cells[0][1], cells[1][1], cells[2][1])
    }

    val thirdColumn by lazy {
        listOf(cells[0][2], cells[1][2], cells[2][2])
    }

    val firstDiagonal by lazy {
        listOf(cells[0][0], cells[1][1], cells[2][2])
    }

    val secondDiagonal by lazy {
        listOf(cells[2][0], cells[1][1], cells[0][2])
    }

    val allRows by lazy {
        listOf(firstRow, secondRow, thirdRow)
    }

    val allLines by lazy {
        listOf(firstRow, secondRow, thirdRow, firstColumn, secondColumn, thirdColumn, firstDiagonal, secondDiagonal)
    }

    val upDownLeftRight by lazy {
        listOf(cells[0][1], cells[1][0], cells[1][2], cells[2][1])
    }

    val corners by lazy {
        listOf(cells[0][0], cells[0][2], cells[2][0], cells[2][2])
    }

    val borders by lazy {
        listOf(cells[0][0], cells[0][1], cells[0][2], cells[1][0], cells[1][2], cells[2][0], cells[2][1], cells[2][2])
    }


    val allCells by lazy {
        listOf(
            cells[0][0],
            cells[0][1],
            cells[0][2],
            cells[1][0],
            cells[1][1],
            cells[1][2],
            cells[2][0],
            cells[2][1],
            cells[2][2]
        )
    }

    fun reset() {
        cells.forEach { row ->
            row.forEach { cell ->
                cell.cellSymbol = TicTacToeSymbol.NONE
            }
        }
    }

    fun getByIndex(index: Int): TicTacToeCell {
        val row = index / 3
        val column = index % 3

        return cells[row][column]
    }

    fun listAvailableCells() =
        sequence {
            cells.forEach { row ->
                row.forEach { cell ->
                    if (cell.isFree()) {
                        yield(cell)
                    }
                }
            }
        }

    fun listMediumPicksFor(playerInTurn: PlayerInTurn) =
        sequence {
            yieldAll(listFreeInBothDirectionsByRow(firstRow))
            yieldAll(listFreeInBothDirectionsByRow(secondRow))
            yieldAll(listFreeInBothDirectionsByRow(thirdRow))
        }.ifEmpty {
            sequence {
                if (firstRow.isAllFree()) {
                    yieldAll(firstRow)
                }
                if (secondRow.isAllFree()) {
                    yieldAll(secondRow)
                }
                if (thirdRow.isAllFree()) {
                    yieldAll(thirdRow)
                }
                if (firstColumn.isAllFree()) {
                    yieldAll(firstColumn)
                }
                if (secondColumn.isAllFree()) {
                    yieldAll(secondColumn)
                }
                if (thirdColumn.isAllFree()) {
                    yieldAll(thirdColumn)
                }
            }
        }.ifEmpty {
            sequence {
                val opponentSymbol = playerInTurn.nextTurn().symbol

                allLines.forEach { line ->
                    val almostOtherPlayerWin = line.almostVictoryOf(opponentSymbol)
                    if (!almostOtherPlayerWin) {
                        yieldAll(line.filterFreeCells())
                    }
                }
            }
        }.ifEmpty {
            sequence {
                cells.forEach { row ->
                    yieldAll(row.toList().filterFreeCells())
                }
            }
        }.toList()

    fun isCenterPickedBy(symbol: TicTacToeSymbol) =
        getByIndex(4).cellSymbol == symbol

    fun listFreeInBothDirections() =
        sequence {
            allRows.forEach { line ->
                yieldAll(listFreeInBothDirectionsByRow(line))
            }
        }.toList()

    fun getCenter(): TicTacToeCell = cells[1][1]

}