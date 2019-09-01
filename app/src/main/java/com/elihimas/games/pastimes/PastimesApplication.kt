package com.elihimas.games.pastimes

import android.app.Application
import com.elihimas.games.pastimes.dagger.ApplicationComponent
import com.elihimas.games.pastimes.dagger.DaggerApplicationComponent
import com.elihimas.games.pastimes.dagger.SettingsModule

class PastimesApplication : Application() {

    companion object {
        lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent.builder()
                .settingsModule(SettingsModule(this))
                .build()
    }

}