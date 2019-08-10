package com.elihimas.games.pastimes.preferences

import android.content.Context
import com.elihimas.games.pastimes.model.GameMode
import javax.inject.Inject

class PastimesPreferences @Inject constructor(context: Context) {

    private companion object {
        const val PREFERENCES_NAME = "pastimes_prefs"
        const val IS_PENDING_CONFIGURATION = "is_pending_configuration"
        const val PREF_GAME_MODE = "pref_game_mode"
        const val PREF_X_VICTORY_COUNT = "pref_x_victory_count"
        const val PREF_O_VICTORY_COUNT = "pref_o_victory_count"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isConfigurationPending() = preferences.getBoolean(IS_PENDING_CONFIGURATION, true)
    fun setConfigurationPendingFalse() = preferences.edit().putBoolean(IS_PENDING_CONFIGURATION, false).commit()

    fun getMode() = GameMode.findModeById(preferences.getInt(PREF_GAME_MODE, GameMode.MEDIUM.id))
    fun setMode(mode: GameMode) = preferences.edit().putInt(PREF_GAME_MODE, mode.id).commit()

    fun getXVictoryCount() = preferences.getInt(PREF_X_VICTORY_COUNT, 0)
    fun setXVictoryCount(count: Int) = preferences.edit().putInt(PREF_X_VICTORY_COUNT, count).commit()

    fun getOVictoryCount() = preferences.getInt(PREF_O_VICTORY_COUNT, 0)
    fun setOVictoryCount(count: Int) = preferences.edit().putInt(PREF_O_VICTORY_COUNT, count).commit()

}
