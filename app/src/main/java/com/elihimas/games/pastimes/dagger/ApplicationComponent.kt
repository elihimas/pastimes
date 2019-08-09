package com.elihimas.games.pastimes.dagger

import com.elihimas.games.pastimes.PastimesApplication
import com.elihimas.games.pastimes.activities.FirstSetupActivity
import com.elihimas.games.pastimes.activities.BasePastimesActivity
import com.elihimas.games.pastimes.activities.StarterActivity
import com.elihimas.games.pastimes.activities.TicTacToeActivity
import com.elihimas.games.pastimes.viewmodel.TicTacToeGameViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SettingsModule::class
    ]
)
interface ApplicationComponent {

    fun inject(pastimesActivity: BasePastimesActivity)
    fun inject(pastimesApplication: PastimesApplication)
    fun inject(loginActivity: StarterActivity)
    fun inject(firstSetupActivity: FirstSetupActivity)
    fun inject(ticTacToeActivity: TicTacToeActivity)
}