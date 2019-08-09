package com.elihimas.games.pastimes.dagger

import com.elihimas.games.pastimes.game.TicTacToeGameController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GamesModule {

    @Provides
    @Singleton
    internal fun provideTicTacToeGameController()=
        TicTacToeGameController()
}
