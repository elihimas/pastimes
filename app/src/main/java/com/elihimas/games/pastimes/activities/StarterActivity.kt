package com.elihimas.games.pastimes.activities

import android.content.Intent
import android.os.Bundle
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

class StarterActivity : BasePastimesActivity() {
    @Inject
    lateinit var preferences: PastimesPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityToBeStarted = if (preferences.isFirstTime()) {
            FirstSetupActivity::class.java
        } else {
            TicTacToeActivity::class.java
        }

        startActivity(Intent(this, activityToBeStarted))
        finish()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
