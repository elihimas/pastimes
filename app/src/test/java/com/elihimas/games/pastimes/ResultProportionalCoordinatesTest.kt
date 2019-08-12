package com.elihimas.games.pastimes

import com.elihimas.games.pastimes.game.TicTacToeCell
import com.elihimas.games.pastimes.views.ProportionalResultCoordinates
import org.junit.Test

import org.junit.Assert.*

class ResultProportionalCoordinatesTest {

    companion object {
        const val ONE_SIXTH = 1f / 6
        const val ONE_THIRD = 1f / 3
    }

    @Test
    fun resultProportionalCoordinates_correctConstructor() {
        fun testCoordinates(
            firstCell: TicTacToeCell, secondCell: TicTacToeCell, thirdCell: TicTacToeCell,
            startX: Float, startY: Float, endX: Float, endY: Float
        ) {
            val coordinates = ProportionalResultCoordinates(listOf(firstCell, secondCell, thirdCell))

            assertEquals("wrong startX", coordinates.startX, startX)
            assertEquals("wrong startY", coordinates.startY, startY)
            assertEquals("wrong endX", coordinates.endX, endX)
            assertEquals("wrong endY", coordinates.endY, endY)
        }

        testCoordinates(
            TicTacToeCell(0, 0),
            TicTacToeCell(0, 1),
            TicTacToeCell(0, 2),
            0f, ONE_SIXTH, 1f, ONE_SIXTH
        )

        testCoordinates(
            TicTacToeCell(1, 0),
            TicTacToeCell(1, 1),
            TicTacToeCell(1, 2),
            0f, ONE_SIXTH + ONE_THIRD, 1f, ONE_SIXTH + ONE_THIRD
        )

        testCoordinates(
            TicTacToeCell(2, 0),
            TicTacToeCell(2, 1),
            TicTacToeCell(2, 2),
            0f, ONE_SIXTH + 2 * ONE_THIRD, 1f, ONE_SIXTH + 2 * ONE_THIRD
        )

        testCoordinates(
            TicTacToeCell(0, 0),
            TicTacToeCell(1, 0),
            TicTacToeCell(2, 0),
            ONE_SIXTH, 0f, ONE_SIXTH, 1f
        )

        testCoordinates(
            TicTacToeCell(0, 1),
            TicTacToeCell(1, 1),
            TicTacToeCell(2, 1),
            ONE_SIXTH + ONE_THIRD, 0f, ONE_SIXTH + ONE_THIRD, 1f
        )

        testCoordinates(
            TicTacToeCell(0, 2),
            TicTacToeCell(1, 2),
            TicTacToeCell(2, 2),
            ONE_SIXTH + 2f / 3, 0f, ONE_SIXTH + 2f / 3, 1f
        )

        testCoordinates(
            TicTacToeCell(0, 0),
            TicTacToeCell(1, 1),
            TicTacToeCell(2, 2),
            0f, 0f, 1f, 1f
        )

        testCoordinates(
            TicTacToeCell(2, 0),
            TicTacToeCell(1, 1),
            TicTacToeCell(0, 2),
            0f, 1f, 1f, 0f
        )
    }
}
