package com.elihimas.games.pastimes.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.activities.BasePastimesActivity
import com.elihimas.games.pastimes.extensions.getEndX
import com.elihimas.games.pastimes.extensions.getEndY
import com.elihimas.games.pastimes.extensions.getStartX
import com.elihimas.games.pastimes.extensions.getStartY
import com.elihimas.games.pastimes.game.GameResult
import com.elihimas.games.pastimes.game.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.tic_tac_toe_game_view.view.*

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

class TicTacToeGameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {

    companion object {
        const val COLUMN_COUNT = 3
        const val ANIMATION_DURATION = 200L
    }

    private var viewModel: TicTacToeGameViewModel
    private lateinit var cells: List<TicTacTorCellView>

    private var proportionalResultCoordinates: ProportionalResultCoordinates? = null
    private var gridAnimationInterpolationValue = 0f
    private var resultLineAnimationInterpolationValue = 0f
    private var itemStrokeWidth = context.resources.getDimension(R.dimen.item_stroke_width)
    private var paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = itemStrokeWidth
        color = context.resources.getColor(R.color.letter_color)
    }

    init {
        val activity = context as BasePastimesActivity

        LayoutInflater.from(context).inflate(R.layout.tic_tac_toe_game_view, this, true)
        setWillNotDraw(false)

        viewModel = ViewModelProviders.of(activity).get(TicTacToeGameViewModel::class.java)
        viewModel.ticTacToeTableData.observe(activity, Observer { table ->
            initCells(table)
            proportionalResultCoordinates = null
            startDrawTableAnimation()
        })
        viewModel.changedCell.observe(activity, Observer { changedCell ->
            updateCell(changedCell)
        })
        viewModel.result.observe(activity, Observer { result ->
            processResult(result)
        })
    }

    private fun initCells(table: TicTacToeTable) {
        cells = listOf(
            cell00.also { cell ->
                cell.init(table.cells[0][0])
                cell.setOnClickListener(this)
            },
            cell01.also { cell ->
                cell.init(table.cells[0][1])
                cell.setOnClickListener(this)
            },
            cell02.also { cell ->
                cell.init(table.cells[0][2])
                cell.setOnClickListener(this)
            },
            cell10.also { cell ->
                cell.init(table.cells[1][0])
                cell.setOnClickListener(this)
            },
            cell11.also { cell ->
                cell.init(table.cells[1][1])
                cell.setOnClickListener(this)
            },
            cell12.also { cell ->
                cell.init(table.cells[1][2])
                cell.setOnClickListener(this)
            },
            cell20.also { cell ->
                cell.init(table.cells[2][0])
                cell.setOnClickListener(this)
            },
            cell21.also { cell ->
                cell.init(table.cells[2][1])
                cell.setOnClickListener(this)
            },
            cell22.also { cell ->
                cell.init(table.cells[2][2])
                cell.setOnClickListener(this)
            }
        )
    }

    private fun updateCell(changedCell: TicTacToeCell) {
        val cellIndex = changedCell.row * COLUMN_COUNT + changedCell.column
        val cell = cells[cellIndex]

        cell.setSymbolAndAnimate(changedCell.cellSymbol)
    }

    private fun processResult(result: GameResult) {
        if (result.winnerSymbol != TicTacToeSymbol.NONE) {
            result.cells?.let { resultCells ->
                proportionalResultCoordinates = ProportionalResultCoordinates(resultCells)
                startDrawResultAnimation()
            }
        }
    }

    override fun onClick(source: View?) {
        val ticTacTorCellView = source as TicTacTorCellView
        val cellData = ticTacTorCellView.getCellData()

        viewModel.onCellClicked(cellData)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val firstLineY = (height / 3).toFloat()
        val secondLineY = (height * 2 / 3).toFloat()
        val firstColumnX = (width / 3).toFloat()
        val secondColumnX = (width * 2 / 3).toFloat()

        canvas?.drawLine(0f, firstLineY, width * gridAnimationInterpolationValue, firstLineY, paint)
        canvas?.drawLine(0f, secondLineY, width * gridAnimationInterpolationValue, secondLineY, paint)
        canvas?.drawLine(firstColumnX, 0f, firstColumnX, width * gridAnimationInterpolationValue, paint)
        canvas?.drawLine(secondColumnX, 0f, secondColumnX, width * gridAnimationInterpolationValue, paint)

        proportionalResultCoordinates?.let { coordinates ->
            val startX = coordinates.startX * width
            val startY = coordinates.startY * height
            val endX = if (coordinates.startX == coordinates.endX) {
                coordinates.endX * width
            } else {
                coordinates.endX * width * resultLineAnimationInterpolationValue
            }
            //TODO refactor this declaration
            val endY =
                if (coordinates.startY == coordinates.endY) {
                    coordinates.endY * height
                } else if (coordinates.endY == 0f) {
                    height * (1 - resultLineAnimationInterpolationValue)
                } else {
                    coordinates.endY * height * resultLineAnimationInterpolationValue
                }

            canvas?.drawLine(startX, startY, endX, endY, paint)
        }
    }

    private fun startDrawTableAnimation() {
        val anim = ValueAnimator.ofFloat(0f, 1f)

        anim.addUpdateListener { valueAnimator ->
            gridAnimationInterpolationValue = valueAnimator.animatedValue as Float
            invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    private fun startDrawResultAnimation() {
        val anim = ValueAnimator.ofFloat(0f, 1f)

        anim.addUpdateListener { valueAnimator ->
            resultLineAnimationInterpolationValue = valueAnimator.animatedValue as Float
            invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }


}
