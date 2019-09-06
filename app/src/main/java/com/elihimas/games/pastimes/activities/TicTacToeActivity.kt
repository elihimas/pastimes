package com.elihimas.games.pastimes.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.SettingsChangeCallbacks
import com.elihimas.games.pastimes.game.SettingsChangeLifecycleObserver
import com.elihimas.games.pastimes.model.GameMode
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import com.elihimas.games.pastimes.views.hideWithAnimation
import com.elihimas.games.pastimes.views.showWithAnimation
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.tic_tac_toe_game.*
import javax.inject.Inject


class TicTacToeActivity : BasePastimesActivity(), SettingsChangeCallbacks {

    @Inject
    lateinit var preferences: PastimesPreferences
    private lateinit var viewModel: TicTacToeGameViewModel

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        init()
    }

    private fun init() {
        fun initUIControls() {
            btReset.setOnClickListener { reset() }
            navigation.setNavigationItemSelectedListener(::onNavItemSelected)

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
            actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                R.string.open_drawer,
                R.string.close_drawer
            ).also { toggle ->
                drawer_layout.addDrawerListener(toggle)
                toggle.syncState()
            }

            verifyClearMenuItemVisibility()
        }

        fun initViewModel() {
            viewModel =
                ViewModelProviders.of(this).get(TicTacToeGameViewModel::class.java).also { model ->
                    model.score.observe(this, Observer { score ->
                        tvScoreX.text = this.getString(R.string.score_x, score.xVictoryCount)
                        tvScoreO.text = this.getString(R.string.score_o, score.oVictoryCount)
                    })
                    model.instructionResId.observe(this, Observer { instruction ->
                        tvInstructions.setText(instruction)
                    })
                    model.suggestion.observe(this, Observer { suggestion ->
                        tvSuggestion.showSuggestion(suggestion.textResId)
                    })
                }
        }

        fun initLifecycleObservers() {
            lifecycle.addObserver(SettingsChangeLifecycleObserver(this))
        }

        initUIControls()
        initViewModel()
        initLifecycleObservers()
    }

    fun verifyClearMenuItemVisibility() {
        val settings = preferences.getSettings()
        navigation.menu.findItem(R.id.menu_item_clear_score).isVisible = settings.recordScore
    }

    override fun startRecordingScore() {
        tvScoreX.showWithAnimation()
        tvScoreO.showWithAnimation()
        verifyClearMenuItemVisibility()
    }

    override fun stopRecordingScore() {
        tvScoreX.hideWithAnimation()
        tvScoreO.hideWithAnimation()
        verifyClearMenuItemVisibility()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun onNavItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)

        fun handleDifficultyChange() {
            val gameMode = when (item.itemId) {
                R.id.menu_item_mode_easy -> GameMode.EASY
                R.id.menu_item_mode_medium -> GameMode.MEDIUM
                R.id.menu_item_mode_impossible -> GameMode.IMPOSSIBLE
                R.id.menu_item_mode_other_player -> GameMode.OTHER_PLAYER
                R.id.menu_item_mode_leaning -> GameMode.LEARNING
                else -> throw IllegalStateException("not implemented for ${item.title}")
            }

            viewModel.changeGameMode(gameMode)
        }

        when {
            item.itemId == R.id.menu_item_settings -> SettingsActivity.start(this)
            item.itemId == R.id.menu_item_clear_score -> viewModel.clearScore()
            else -> handleDifficultyChange()
        }

        return true
    }

    private fun reset() {
        viewModel.reset()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
