package com.elihimas.games.pastimes.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.activities.BasePastimesActivity
import com.elihimas.games.pastimes.game.GameResult
import com.elihimas.games.pastimes.model.SuggestionType
import com.elihimas.games.pastimes.model.TicTacToeCell
import com.elihimas.games.pastimes.model.TicTacToeSymbol
import com.elihimas.games.pastimes.model.TicTacToeTable
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.tic_tac_toe_game_view.view.*

class TicTacToeGameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {

    companion object {
        const val COLUMN_COUNT = 3
    }

    private var viewModel: TicTacToeGameViewModel
    private lateinit var cells: List<TicTacTorCellView>

    private val canvasPainter = CanvasPainter(context)

    init {
        val activity = context as BasePastimesActivity

        LayoutInflater.from(context).inflate(R.layout.tic_tac_toe_game_view, this, true)
        setWillNotDraw(false)

        viewModel = ViewModelProviders.of(activity).get(TicTacToeGameViewModel::class.java)
        viewModel.ticTacToeTableData.observe(activity, Observer { table ->
            initCells(table)
            canvasPainter.proportionalResultCoordinates = null
            canvasPainter.startDrawTableAnimation(this)
        })
        viewModel.changedCell.observe(activity, Observer { changedCell ->
            updateCell(changedCell)
        })
        viewModel.result.observe(activity, Observer { result ->
            processResult(result)
        })
        viewModel.suggestion.observe(activity, Observer { suggestion ->
            if (suggestion.betterPicks.isNotEmpty()) {
                showMissedWin(suggestion.betterPicks)
            }
        })
    }

    private fun showMissedWin(betterPicks: List<TicTacToeCell>) {
        canvasPainter.betterPicksToSuggest = betterPicks

        canvasPainter.startDrawSuggestionAnimation(this)
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
                canvasPainter.proportionalResultCoordinates = ProportionalResultCoordinates(resultCells)
                canvasPainter.startDrawResultAnimation(this)
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

        canvasPainter.drawGrid(canvas, width, height)
        canvasPainter.drawResults(canvas, width, height)
        canvasPainter.drawSuggestions(canvas, cells)
    }


}
