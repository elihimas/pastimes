package com.elihimas.games.pastimes.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import java.util.*
import kotlin.concurrent.schedule

class SuggestionText(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {

    private companion object {
        const val SUGGESTION_DISPLAY_TIME = 3000L
    }

    private val timer = Timer()
    private var currentScheduler: TimerTask? = null


    fun showSuggestion(textResId: Int) {
        setText(textResId)

        currentScheduler?.cancel()
        currentScheduler = timer.schedule(SUGGESTION_DISPLAY_TIME) {
            (context as Activity).runOnUiThread {
                text = ""
            }
        }

    }
}
