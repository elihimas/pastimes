package com.elihimas.games.pastimes.activities

import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.GameMode
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import java.lang.IllegalStateException


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tic_tac_toe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val gameMode = when (item.itemId) {
            R.id.menu_item_difficulty_easy -> GameMode.EASY
            R.id.menu_item_difficulty_medium -> GameMode.MEDIUM
            R.id.menu_item_difficulty_impossible -> GameMode.IMPOSSIBLE
            R.id.menu_item_difficulty_other_player -> GameMode.OTHER_PLAYER
            else -> throw IllegalStateException("not implemented for ${item.title}")
        }

        viewModel.setGameMode(gameMode)

        return true
    }

    private fun reset() {
        viewModel.reset()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
