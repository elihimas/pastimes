package com.elihimas.games.pastimes.dagger

import com.elihimas.games.pastimes.PastimesApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: PastimesApplication) {

    @Provides
    @Singleton
    fun provideContext() = app
}