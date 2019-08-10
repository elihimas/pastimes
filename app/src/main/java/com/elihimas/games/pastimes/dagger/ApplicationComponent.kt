package com.elihimas.games.pastimes.dagger

import com.elihimas.games.pastimes.activities.FirstSetupActivity
import com.elihimas.games.pastimes.activities.StarterActivity
import com.elihimas.games.pastimes.game.TicTacToeGameController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SettingsModule::class
    ]
)
interface ApplicationComponent {

    fun inject(loginActivity: StarterActivity)
    fun inject(firstSetupActivity: FirstSetupActivity)
    fun inject(ticTacToeGameController: TicTacToeGameController)
}