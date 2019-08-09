package com.elihimas.games.pastimes.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.R

class TicTacToeActivity : BasePastimesActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        init()
    }

    private fun init() {
        val initSpinner = fun() {
        }

        initSpinner()
    }

    override fun injectDagger() {
        PastimesApplication.appComponent.inject(this)
    }
}
