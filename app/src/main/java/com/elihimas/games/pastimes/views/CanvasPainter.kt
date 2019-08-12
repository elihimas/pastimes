package com.elihimas.games.pastimes.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.animation.doOnEnd
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.TicTacToeCell

class CanvasPainter(context: Context) {

    companion object {
        private const val START_ANGLE = -180f
        private const val SWEEP_ANGLE = 360

        private const val ANIMATION_MAX_VALUE = 1f
        private const val ANIMATION_ALPHA_VALUE = 255f

        const val ANIMATION_DURATION = 400L
        private const val SUGGESTION_ANIMATION_DURATION = 800L
    }

    private var itemStrokeWidth = context.resources.getDimension(R.dimen.item_stroke_width)
    private var normalPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = itemStrokeWidth
        color = context.resources.getColor(R.color.letter_color)
    }

    private var suggestionPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.letter_color)
    }

    var symbolInterpolationValue = 0f
    var gridAnimationInterpolationValue = 0f
    var resultLineAnimationInterpolationValue = 0f
    var suggestionAnimationInterpolationValue = 0f
    var proportionalResultCoordinates: ProportionalResultCoordinates? = null
    var betterPicksToSuggest: List<TicTacToeCell>? = null

    fun drawO(canvas: Canvas?, width: Int, height: Int) {
        canvas?.drawArc(
            itemStrokeWidth / 2,
            itemStrokeWidth / 2,
            width - itemStrokeWidth / 2,
            height - itemStrokeWidth / 2,
            START_ANGLE,
            symbolInterpolationValue * SWEEP_ANGLE,
            false,
            normalPaint
        )
    }

    fun drawX(canvas: Canvas?, width: Int, height: Int) {
        doDrawX(canvas, width, height, symbolInterpolationValue)
    }

    private fun doDrawX(
        canvas: Canvas?,
        width: Int,
        height: Int,
        animationValue: Float,
        left: Float = 0f,
        top: Float = 0f,
        paint: Paint = normalPaint
    ) {
        canvas?.drawLine(
            left,
            top,
            width * animationValue + left,
            height * animationValue + top,
            paint
        )

        canvas?.drawLine(
            left,
            height.toFloat() + top,
            width * symbolInterpolationValue + left,
            height * (1 - symbolInterpolationValue) + top,
            paint
        )
    }

    fun drawGrid(canvas: Canvas?, width: Int, height: Int) {
        val firstLineY = (height / 3).toFloat()
        val secondLineY = (height * 2 / 3).toFloat()
        val firstColumnX = (width / 3).toFloat()
        val secondColumnX = (width * 2 / 3).toFloat()

        canvas?.drawLine(0f, firstLineY, width * gridAnimationInterpolationValue, firstLineY, normalPaint)
        canvas?.drawLine(0f, secondLineY, width * gridAnimationInterpolationValue, secondLineY, normalPaint)
        canvas?.drawLine(firstColumnX, 0f, firstColumnX, width * gridAnimationInterpolationValue, normalPaint)
        canvas?.drawLine(secondColumnX, 0f, secondColumnX, width * gridAnimationInterpolationValue, normalPaint)
    }

    fun drawResults(canvas: Canvas?, width: Int, height: Int) {
        proportionalResultCoordinates?.let { coordinates ->
            val startX = coordinates.startX * width
            val startY = coordinates.startY * height
            val endX = if (coordinates.startX == coordinates.endX) {
                coordinates.endX * width
            } else {
                coordinates.endX * width * resultLineAnimationInterpolationValue
            }
            val endY =
                when {
                    coordinates.startY == coordinates.endY -> coordinates.endY * height
                    coordinates.endY == 0f -> height * (1 - resultLineAnimationInterpolationValue)
                    else -> coordinates.endY * height * resultLineAnimationInterpolationValue
                }

            canvas?.drawLine(startX, startY, endX, endY, normalPaint)
        }
    }

    fun drawSuggestions(
        canvas: Canvas?,
        cellsViews: List<TicTacTorCellView>
    ) {
        betterPicksToSuggest?.forEach { ticTacToeCell: TicTacToeCell ->
            val index = ticTacToeCell.column + ticTacToeCell.row * 3
            val cellView = cellsViews[index]
            drawSquare(
                canvas,
                cellView.width,
                cellView.height,
                suggestionAnimationInterpolationValue,
                cellView.left.toFloat(),
                cellView.top.toFloat(),
                suggestionPaint
            )
        }
        suggestionPaint.alpha = suggestionAnimationInterpolationValue.toInt()
    }

    private fun drawSquare(
        canvas: Canvas?,
        width: Int,
        height: Int,
        interpolationValue: Float,
        left: Float,
        top: Float,
        paint: Paint
    ) {
        canvas?.drawRect(left, top, left + width, top + height, paint)
    }

    fun startSymbolAnimation(viewToInvalidate: View) {
        val anim = ValueAnimator.ofFloat(0f, 1f)

        anim.addUpdateListener { valueAnimator ->
            symbolInterpolationValue = valueAnimator.animatedValue as Float
            viewToInvalidate.invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    fun startDrawTableAnimation(viewToInvalidate: View) {
        val anim = ValueAnimator.ofFloat(0f, ANIMATION_MAX_VALUE)

        anim.addUpdateListener { valueAnimator ->
            gridAnimationInterpolationValue = valueAnimator.animatedValue as Float
            viewToInvalidate.invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    fun startDrawResultAnimation(viewToInvalidate: View) {
        val anim = ValueAnimator.ofFloat(0f, ANIMATION_MAX_VALUE)

        anim.addUpdateListener { valueAnimator ->
            resultLineAnimationInterpolationValue = valueAnimator.animatedValue as Float
            viewToInvalidate.invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    fun startDrawSuggestionAnimation(viewToInvalidate: View) {
        val anim = ValueAnimator.ofFloat(0f, ANIMATION_ALPHA_VALUE, 0f)

        anim.addUpdateListener { valueAnimator ->
            suggestionAnimationInterpolationValue = valueAnimator.animatedValue as Float
            viewToInvalidate.invalidate()
        }
        anim.doOnEnd {
            betterPicksToSuggest = null
        }

        anim.duration = SUGGESTION_ANIMATION_DURATION
        anim.start()
    }
}