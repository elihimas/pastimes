package com.elihimas.games.pastimes.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.elihimas.games.pastimes.activities.BasePastimesActivity
import java.util.*
import kotlin.concurrent.schedule

class SuggestionText(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {

    private companion object {
        const val SUGGESTION_DISPLAY_TIME = 2000L
        const val ANIMATION_DURATION = 300L
    }

    private val timer = Timer()
    private var currentScheduler: TimerTask? = null


    fun showSuggestion(textResId: Int) {
        setText(textResId)
        animate().setDuration(ANIMATION_DURATION).scaleX(1.5f).scaleY(1.5f).alpha(1f).start()

        currentScheduler?.cancel()
        currentScheduler = timer.schedule(SUGGESTION_DISPLAY_TIME) {
            (context as BasePastimesActivity).runOnUiThread {
                animate().setDuration(ANIMATION_DURATION).scaleX(1f).scaleY(1f).alpha(0f).start()
            }
        }

    }
}
