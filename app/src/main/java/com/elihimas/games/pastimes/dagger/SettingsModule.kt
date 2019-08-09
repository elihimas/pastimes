package com.elihimas.games.pastimes.dagger

import android.content.Context
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import dagger.Module
import dagger.Provides

@Module
class SettingsModule(private val context: Context) {

    @Provides
    internal fun providePastimesPreferences(): PastimesPreferences =
        PastimesPreferences(context)
}
