package com.elihimas.games.pastimes.game

import com.elihimas.games.pastimes.extensions.*
import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.model.PlayerInTurn
import com.elihimas.games.pastimes.model.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable

private interface IA {
    fun pickCell(playerInTurn: PlayerInTurn, currentTurn: Int): TicTacToeCell?
}

private class EasyIA(val gameTable: TicTacToeTable) : IA {

    override fun pickCell(playerInTurn: PlayerInTurn, currentTurn: Int): TicTacToeCell? {
        val opponentSymbol = playerInTurn.nextTurn().symbol

        val possiblePicks = if (currentTurn == 1) {
            when {
                gameTable.isCenterPickedBy(opponentSymbol) -> gameTable.upDownLeftRight
                gameTable.corners.hasAPickBy(opponentSymbol) -> gameTable.borders.filterFreeCells()
                else -> gameTable.listFreeInBothDirections()
            }
        } else if (currentTurn == 3 && !gameTable.isCenterPickedBy(opponentSymbol)) {
            gameTable.borders.filterFreeCells()
        } else {
            sequence {
                val noVictoryLines = gameTable.allLines.filter { line -> !line.almostVictory() }
                yieldAll(noVictoryLines.filterFreeCells())
            }.ifEmpty {
                sequence {
                    val noMyVictoryLines = gameTable.allLines.filter { line -> !line.almostVictory() }
                    yieldAll(noMyVictoryLines.filterFreeCells())
                }
            }.toList()
        }.ifEmpty {
            gameTable.allLines.filterFreeCells().toList()
        }

        return possiblePicks.pickRandom()
    }
}


private class MediumIA(val gameTable: TicTacToeTable) : IA {

    override fun pickCell(playerInTurn: PlayerInTurn, currentTurn: Int): TicTacToeCell? {
        val picks =
            run {
                val almostWinLine =
                    gameTable.allLines.firstOrNull { line -> line.almostVictoryOf(playerInTurn.symbol) }
                almostWinLine?.filterFreeCells()?.toList()
            } ?: run {
                if (currentTurn == 3) {
                    val almostOpponentWinLine =
                        gameTable.allLines.firstOrNull { line -> line.almostVictoryOf(playerInTurn.nextTurn().symbol) }

                    almostOpponentWinLine?.filterFreeCells()?.toList()
                } else {
                    null
                }
            }
            ?: gameTable.listMediumPicksFor(playerInTurn)

        return picks.random()
    }
}

class ImpossibleIA(private val gameTable: TicTacToeTable) : IA {

    override fun pickCell(playerInTurn: PlayerInTurn, currentTurn: Int): TicTacToeCell? {
        val opponentSymbol = playerInTurn.nextTurn().symbol

        fun myMove() = gameTable.allCells.first { cell -> cell.cellSymbol == playerInTurn.symbol }

        fun isUpDownLeftRightWithTwoOpponentClicks() =
            gameTable.upDownLeftRight.hasTwoPicksBy(opponentSymbol)

        fun getCornerWithNoFreeNeighbours(): TicTacToeCell {
            val row = if (gameTable.cells[0][1].isTaken()) {
                0
            } else {
                2
            }
            val column = if (gameTable.cells[1][0].isTaken()) {
                0
            } else {
                2
            }

            return gameTable.cells[row][column]
        }

        fun cellsInLinesWithOnlyThisCellTaken(cell: TicTacToeCell) =
            sequence {
                gameTable.allLines.filter { line ->
                    line.contains(cell) && line.hasTwoPicksBy(TicTacToeSymbol.NONE)
                }.forEach { line ->
                    yieldAll(line.filterFreeCells())
                }
            }.toList()

        val possiblePicks: List<TicTacToeCell> =
            run {
                run {
                    val almostWinLine =
                        gameTable.allLines.firstOrNull { line -> line.almostVictoryOf(playerInTurn.symbol) }
                    almostWinLine?.filterFreeCells()?.toList()
                }
                    ?: run {
                        val almostOpponentWinLine =
                            gameTable.allLines.firstOrNull { line -> line.almostVictoryOf(playerInTurn.nextTurn().symbol) }

                        almostOpponentWinLine?.filterFreeCells()?.toList()
                    }
                    ?: run {
                        if (currentTurn == 1) {
                            when {
                                gameTable.isCenterPickedBy(opponentSymbol) -> gameTable.corners
                                gameTable.corners.hasAPickBy(opponentSymbol) -> listOf(gameTable.getCenter())
                                else -> {
                                    val opponentMove = gameTable.allCells.first { cell -> cell.isTaken() }
                                    listOf(opponentMove.otherSide())
                                }
                            }.toList()
                        } else {
                            null
                        }
                    }
                    ?: run {
                        if (currentTurn == 3) {
                            when {
                                gameTable.isCenterPickedBy(opponentSymbol)
                                        && gameTable.upDownLeftRight.hasAPickBy(opponentSymbol)
                                -> myMove().neighbours().filterFreeCells()

                                isUpDownLeftRightWithTwoOpponentClicks()
                                -> listOf(getCornerWithNoFreeNeighbours())

                                else -> gameTable.allCells.filterFreeCells()
                            }.toList()
                        } else {
                            null
                        }
                    }
                    ?: run {
                        if (currentTurn == 5) {
                            val myTwoMoves =
                                gameTable.allCells.filter { cell -> cell.cellSymbol == playerInTurn.symbol }
                            val freeLines1 = cellsInLinesWithOnlyThisCellTaken(myTwoMoves[0])
                            val freeLines2 = cellsInLinesWithOnlyThisCellTaken(myTwoMoves[1])

                            freeLines1.intersect(freeLines2).toList()
                        } else {
                            null
                        }
                    }
                    ?: gameTable.allCells.filterFreeCells().toList()
            }
                .ifEmpty {
                    gameTable.allCells.filterFreeCells().toList()
                }

        return possiblePicks.pickRandom()
    }
}

class TicTacToeAI {

    private lateinit var gameTable: TicTacToeTable
    private var ia: IA? = null

    fun configure(gameTable: TicTacToeTable, gameMode: GameMode) {
        this.gameTable = gameTable

        ia = when (gameMode) {
            GameMode.EASY -> EasyIA(gameTable)
            GameMode.MEDIUM -> MediumIA(gameTable)
            GameMode.IMPOSSIBLE -> ImpossibleIA(gameTable)
            else -> null
        }
    }

    fun pickCell(playerInTurn: PlayerInTurn, currentTurn: Int) = ia?.pickCell(playerInTurn, currentTurn)

}