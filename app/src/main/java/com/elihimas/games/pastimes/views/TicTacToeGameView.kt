package com.elihimas.games.pastimes.views

import android.content.Context
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

    private var viewModel: TicTacToeGameViewModel

    private lateinit var cells: List<GameCellView>

    init {
        val activity = context as BasePastimesActivity

        LayoutInflater.from(context).inflate(R.layout.tic_tac_toe_game_view, this, true)

        viewModel = ViewModelProviders.of(activity).get(TicTacToeGameViewModel::class.java)
        viewModel.ticTacToeTableData.observe(activity, Observer { table ->
            initCells(table)
        })
        viewModel.changedCell.observe(activity, Observer { changedCell ->
            updateCell(changedCell)
        })
    }

    private fun updateCell(changedCellData: CellData) {
        val cellIndex = changedCellData.row * 3 + changedCellData.column
        val cell = cells[cellIndex]

        cell.setSymbolAndAnimate(changedCellData.cellSymbol)
    }


    private fun initCells(table: TicTacToeTable) {
        cells = listOf(
            cell00.apply { init(table.cells[0][0]) },
            cell01.apply { init(table.cells[0][1]) },
            cell02.apply { init(table.cells[0][2]) },
            cell10.apply { init(table.cells[1][0]) },
            cell11.apply { init(table.cells[1][1]) },
            cell12.apply { init(table.cells[1][2]) },
            cell20.apply { init(table.cells[2][0]) },
            cell21.apply { init(table.cells[2][1]) },
            cell22.apply { init(table.cells[2][2]) }
        )
        cells.forEach { view ->
            view.setOnClickListener(this)
        }
    }

    override fun onClick(source: View?) {
        viewModel.onCellClicked(source?.tag as CellData)
    }

}