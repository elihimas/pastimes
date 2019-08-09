package com.elihimas.games.pastimes.dagger

import android.content.Context
import com.elihimas.games.pastimes.game.TicTacToeGameController
import com.elihimas.games.pastimes.preferences.PastimesPreferences
import dagger.Module
import dagger.Provides

@Module
class GamesModule {

    @Provides
    internal fun provideTicTacToeGameController()=
        TicTacToeGameController()
}
