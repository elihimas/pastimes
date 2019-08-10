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
import com.elihimas.games.pastimes.game.CellData
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.tic_tac_toe_game_view.view.*

class TicTacToeGameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {

    companion object {
        const val COLUMN_COUNT = 3
        const val ANIMATION_DURATION = 8000L
    }

    private var viewModel: TicTacToeGameViewModel
    private lateinit var cells: List<TicTacTorCellView>

    private var animationInterpolationValue = 0f
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
            showTableLines()
        })
        viewModel.changedCell.observe(activity, Observer { changedCell ->
            updateCell(changedCell)
        })
    }

    private fun updateCell(changedCellData: CellData) {
        val cellIndex = changedCellData.row * COLUMN_COUNT + changedCellData.column
        val cell = cells[cellIndex]

        cell.setSymbolAndAnimate(changedCellData.cellSymbol)
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

        canvas?.drawLine(0f, firstLineY, width * animationInterpolationValue, firstLineY, paint)
        canvas?.drawLine(0f, secondLineY, width * animationInterpolationValue, secondLineY, paint)
        canvas?.drawLine(firstColumnX, 0f, firstColumnX, width * animationInterpolationValue, paint)
        canvas?.drawLine(secondColumnX, 0f, secondColumnX, width * animationInterpolationValue, paint)
    }

    private fun showTableLines() {
        val anim = ValueAnimator.ofFloat(0f, 100f)

        anim.addUpdateListener { valueAnimator ->
            animationInterpolationValue = valueAnimator.animatedValue as Float
            invalidate()
        }

        anim.duration = ANIMATION_DURATION
        anim.start()
    }


}
