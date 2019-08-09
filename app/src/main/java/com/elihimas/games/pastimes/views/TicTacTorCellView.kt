package com.elihimas.games.pastimes.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.CellData
import com.elihimas.games.pastimes.game.TicTacToeSymbol

class TicTacTorCellView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private companion object {
        const val START_ANGLE = -180f
        const val END_ANGLE = 180
        const val ANIMATION_DURATION = 2000L
    }

    private var itemStrokeWidth = context.resources.getDimension(R.dimen.item_stroke_width)
    private var paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = itemStrokeWidth
        color = context.resources.getColor(R.color.letter_color)
    }
    private var animationInterpolationValue = 0f
    private var symbol = TicTacToeSymbol.EMPTY

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val drawO = fun() {
            canvas?.drawArc(
                itemStrokeWidth / 2,
                itemStrokeWidth / 2,
                width - itemStrokeWidth / 2,
                height - itemStrokeWidth / 2,
                START_ANGLE,
                animationInterpolationValue * END_ANGLE,
                false,
                paint
            )
        }

        val drawX = fun() {
            canvas?.drawLine(
                0f,
                0f,
                width * animationInterpolationValue,
                height * animationInterpolationValue,
                paint
            )

            canvas?.drawLine(
                0f,
                height.toFloat(),
                width * animationInterpolationValue,
                height * (1 - animationInterpolationValue),
                paint
            )
        }

        when (symbol) {
            TicTacToeSymbol.O_SYMBOL -> drawO()
            TicTacToeSymbol.X_SYMBOL -> drawX()
        }
    }

    private fun startAnimation() {
        val anim = ValueAnimator.ofFloat(0f, 100f)

        anim.addUpdateListener { valueAnimator ->
            animationInterpolationValue = valueAnimator.animatedValue as Float
            invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    fun setSymbolAndAnimate(cellSymbol: TicTacToeSymbol) {
        symbol = cellSymbol

        if (symbol != TicTacToeSymbol.EMPTY) {
            startAnimation()
        }else{
            invalidate()
        }
    }

    fun init(cellData: CellData) {
        tag = cellData
        setSymbolAndAnimate(TicTacToeSymbol.EMPTY)
    }
}