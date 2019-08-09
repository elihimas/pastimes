package com.elihimas.games.pastimes.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import com.elihimas.games.pastimes.R
import com.elihimas.games.pastimes.activities.BasePastimesActivity
import com.elihimas.games.pastimes.viewmodel.TicTacToeViewModel

class TicTacToeGameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        val activity = context as BasePastimesActivity

        LayoutInflater.from(context).inflate(R.layout.tic_tac_toe_game_view, this, true)
        val viewModel = ViewModelProviders.of(activity).get(TicTacToeViewModel::class.java)
    }

}