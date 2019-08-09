package com.elihimas.games.pastimes.preferences

import android.content.Context
import com.elihimas.games.pastimes.game.GameMode
import javax.inject.Inject

class PastimesPreferences @Inject constructor(context: Context) {

    private companion object {
        const val PREFERENCES_NAME = "pastimes_prefs"
        const val PREF_IS_FIRST_TIME = "pref_is_first_time"
        const val PREF_GAME_MODE = "pref_game_mode"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isFirstTime() = preferences.getBoolean(PREF_IS_FIRST_TIME, true)

    fun setFirstTimeFalse() = preferences.edit().putBoolean(PREF_IS_FIRST_TIME, false).commit()

    fun getMode() = GameMode.values()[preferences.getInt(PREF_GAME_MODE, GameMode.MEDIUM.ordinal)]

    fun setMode(mode: GameMode) = preferences.edit().putInt(PREF_GAME_MODE, mode.ordinal).commit()
}
