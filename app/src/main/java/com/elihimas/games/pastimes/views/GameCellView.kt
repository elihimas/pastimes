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

class GameCellView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private companion object {
        val START_ANGLE = -180f
        val END_ANGLE = 180
        val ANIMATION_DURATION = 2000L
    }

    private var stroke = context.resources.getDimension(R.dimen.item_stroke_width)
    private var paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = stroke
        color = context.resources.getColor(R.color.letter_color)
    }
    private var animationInterpolationValue = 0f
    private var symbol = TicTacToeSymbol.EMPTY

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val drawO = fun() {
            canvas?.drawArc(
                stroke / 2,
                stroke / 2,
                width - stroke / 2,
                height - stroke / 2,
                START_ANGLE,
                animationInterpolationValue * END_ANGLE,
                false,
                paint
            )
        }

        val drawX = fun() {
            Log.d("drawX", "$animationInterpolationValue ${width * animationInterpolationValue}, ${height * animationInterpolationValue}")

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