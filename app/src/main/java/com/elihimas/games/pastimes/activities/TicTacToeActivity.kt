package com.elihimas.games.pastimes.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*

import java.util.*
import kotlin.concurrent.schedule

class TicTacToeActivity : BasePastimesActivity() {

    private companion object {
        const val SUGGESTION_DISPLAY_TIME = 3000L
    }

    private lateinit var viewModel: TicTacToeGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        init()
    }

    private fun init() {
        fun initControls() {
            btReset.setOnClickListener { reset() }
        }

        fun initViewModel() {
            viewModel = ViewModelProviders.of(this).get(TicTacToeGameViewModel::class.java)
            viewModel.score.observe(this, Observer { score ->
                tvScoreX.text = this.getString(R.string.score_x, score.xVictoryCount)
                tvScoreO.text = this.getString(R.string.score_o, score.oVictoryCount)
            })
            viewModel.instructionResId.observe(this, Observer { instruction ->
                tvInstructions.setText(instruction)
            })

            viewModel.suggestion.observe(this, Observer { suggestion ->
                tvSuggestion.setText(suggestion.textResId)

                Timer().schedule(SUGGESTION_DISPLAY_TIME) {
                    runOnUiThread {
                        tvSuggestion.text = ""
                    }
                }
            })
        }

        initControls()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tic_tac_toe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val gameMode = when (item.itemId) {
            R.id.menu_item_mode_easy -> GameMode.EASY
            R.id.menu_item_mode_medium -> GameMode.MEDIUM
            R.id.menu_item_mode_impossible -> GameMode.IMPOSSIBLE
            R.id.menu_item_mode_other_player -> GameMode.OTHER_PLAYER
            R.id.menu_item_mode_leaning -> GameMode.LEARNING
            else -> throw IllegalStateException("not implemented for ${item.title}")
        }

        viewModel.changeGameMode(gameMode)

        return true
    }

    private fun reset() {
        viewModel.reset()
    }

    override fun injectDagger() {
        //nothing to do
    }
}
