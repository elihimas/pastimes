package com.elihimas.games.pastimes.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.game.GameMode
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import kotlinx.android.synthetic.main.activity_dificulty.*
import javax.inject.Inject

class FirstSetupActivity : BasePastimesActivity(), View.OnClickListener {

    @Inject
    lateinit var preferences: PastimesPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dificulty)

        btEasy.setOnClickListener(this)
        btMedium.setOnClickListener(this)
        btImpossible.setOnClickListener(this)
        btOtherPlayer.setOnClickListener(this)
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }

    override fun onClick(source: View?) {
        val mode = when (source) {
            btEasy -> GameMode.EASY
            btMedium -> GameMode.MEDIUM
            btImpossible -> GameMode.IMPOSSIBLE
            btOtherPlayer -> GameMode.OTHER_PLAYER
            else -> throw IllegalStateException("not implemented for $source")
        }

        preferences.setMode(mode)
        preferences.setFirstTimeFalse()

        startActivity(Intent(this, TicTacToeActivity::class.java))
        finish()
    }
}
