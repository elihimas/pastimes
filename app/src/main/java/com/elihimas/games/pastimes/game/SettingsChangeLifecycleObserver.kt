package com.elihimas.games.pastimes.game

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import javax.inject.Inject

class SettingsChangeLifecycleObserver(private val settingsChangeCallbacks: SettingsChangeCallbacks) :
    LifecycleObserver {

    @Inject
    lateinit var preferences: PastimesPreferences
    private var settings: Settings

    init {
        PastimesApplication.appComponent.inject(this)
        settings = preferences.getSettings()

        if (settings.recordScore) {
            settingsChangeCallbacks.startRecordingScore()
        } else {
            settingsChangeCallbacks.stopRecordingScore()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val newSettings = preferences.getSettings()

        if (settings.recordScore != newSettings.recordScore) {
            if (newSettings.recordScore) {
                settingsChangeCallbacks.startRecordingScore()
            } else {
                settingsChangeCallbacks.stopRecordingScore()
            }
        }

        settings = newSettings
    }

}

interface SettingsChangeCallbacks {
    fun startRecordingScore()
    fun stopRecordingScore()
}
