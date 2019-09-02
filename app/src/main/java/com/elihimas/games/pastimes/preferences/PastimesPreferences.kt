package com.elihimas.games.pastimes.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.elihimas.games.pastimes.game.Settings
import com.elihimas.games.pastimes.model.GameMode
import javax.inject.Inject

class PastimesPreferences @Inject constructor(context: Context) {

    private companion object {
        const val IS_PENDING_CONFIGURATION = "is_pending_configuration"
        const val PREF_GAME_MODE = "pref_game_mode"
        const val PREF_X_VICTORY_COUNT = "pref_x_victory_count"
        const val PREF_O_VICTORY_COUNT = "pref_o_victory_count"
        const val RECORD_SCORE = "record_score"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isConfigurationPending() = preferences.getBoolean(IS_PENDING_CONFIGURATION, true)
    fun setConfigurationPendingFalse() =
        preferences.edit().putBoolean(IS_PENDING_CONFIGURATION, false).commit()

    fun getMode() = GameMode.findModeById(preferences.getInt(PREF_GAME_MODE, GameMode.MEDIUM.id))
    fun setMode(mode: GameMode) = preferences.edit().putInt(PREF_GAME_MODE, mode.id).commit()

    fun getXVictoryCount() = preferences.getInt(PREF_X_VICTORY_COUNT, 0)
    fun setXVictoryCount(count: Int) =
        preferences.edit().putInt(PREF_X_VICTORY_COUNT, count).commit()

    fun getOVictoryCount() = preferences.getInt(PREF_O_VICTORY_COUNT, 0)
    fun setOVictoryCount(count: Int) =
        preferences.edit().putInt(PREF_O_VICTORY_COUNT, count).commit()

    fun resetVictoryCount() {
        setXVictoryCount(0)
        setOVictoryCount(0)
    }

    fun getRecordScore() = preferences.getBoolean(RECORD_SCORE, true)

    fun getSettings() = Settings(getRecordScore())

}
