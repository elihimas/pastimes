package com.elihimas.games.pastimes.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*

class TicTacToeActivity : BasePastimesActivity() {

    private lateinit var viewModel: TicTacToeGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        init()
    }

    private fun init() {
        val initControls = fun() {
            btReset.setOnClickListener { reset() }
        }
        val initViewModel = fun() {
            viewModel = ViewModelProviders.of(this).get(TicTacToeGameViewModel::class.java)

            viewModel.score.observe(this, Observer { score ->
                tvScoreX.text = this.getString(R.string.score_x, score.xVictoryCount)
                tvScoreO.text = this.getString(R.string.score_o, score.oVictoryCount)
            })
            viewModel.instructionResId.observe(this, Observer { instruction ->
                tvInstructions.setText(instruction)
            })
        }

        initControls()
        initViewModel()
    }

    private fun reset() {
        viewModel.reset()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
