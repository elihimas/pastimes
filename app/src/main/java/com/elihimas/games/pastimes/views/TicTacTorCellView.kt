package com.elihimas.games.pastimes.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.elihimas.games.pastimes.model.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeSymbol

class TicTacTorCellView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var symbol = TicTacToeSymbol.NONE

    private val canvasPainter = CanvasPainter(context)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        when (symbol) {
            TicTacToeSymbol.O_SYMBOL -> canvasPainter.drawO(canvas, width, height)
            TicTacToeSymbol.X_SYMBOL -> canvasPainter.drawX(canvas, width, height)
        }
    }

    fun setSymbolAndAnimate(cellSymbol: TicTacToeSymbol) {
        symbol = cellSymbol

        if (symbol != TicTacToeSymbol.NONE) {
            canvasPainter.startSymbolAnimation(this)
        } else {
            invalidate()
        }
    }

    fun init(cell: TicTacToeCell) {
        tag = cell
        setSymbolAndAnimate(TicTacToeSymbol.NONE)
    }

    fun getCellData(): TicTacToeCell = tag as TicTacToeCell
}