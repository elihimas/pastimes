package com.elihimas.games.pastimes.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.game.TicTacToeSymbol
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import javax.inject.Inject

class TicTacToeActivity : BasePastimesActivity() {

    @Inject
    lateinit var game: TicTacToeGameController

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
            val viewModel = ViewModelProviders.of(this).get(TicTacToeGameViewModel::class.java)

            viewModel.winnerSymbol.observe(this, Observer { winnerSymbol ->
                val winnerTextResId = if (winnerSymbol == TicTacToeSymbol.O_SYMBOL) {
                    R.string.winner_o_prayse_message
                } else {
                    R.string.winner_x_prayse_message
                }

                tvResults.setText(winnerTextResId)
            })
        }

        initControls()
        initViewModel()
    }

    private fun reset() {
        game.reset()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
