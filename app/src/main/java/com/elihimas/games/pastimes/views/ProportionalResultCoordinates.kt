package com.elihimas.games.pastimes.views

import com.elihimas.games.pastimes.extensions.getEndX
import com.elihimas.games.pastimes.extensions.getEndY
import com.elihimas.games.pastimes.extensions.getStartX
import com.elihimas.games.pastimes.extensions.getStartY
import com.elihimas.games.pastimes.model.TicTacToeCell

data class ProportionalResultCoordinates constructor(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
) {
    constructor(cells: List<TicTacToeCell>) : this(
        cells.getStartX(),
        cells.getStartY(),
        cells.getEndX(),
        cells.getEndY()
    )
}
