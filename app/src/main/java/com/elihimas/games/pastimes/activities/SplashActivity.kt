package com.elihimas.games.pastimes.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.animation.addListener
import com.elihimas.games.pastimes.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private companion object {
        private const val START_DELAY = 1000L
        private const val DURATION = 1200L
    }

    private val finishRunnable = Runnable {
        startActivity(Intent(applicationContext, StarterActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        oIcon.iconAnimation(DURATION * 2)
        xIcon.iconAnimation(DURATION, finishRunnable)

        ObjectAnimator.ofArgb(xIcon, "alpha", 0xbb, 0xcc, 0xee, 0xff).setDuration(DURATION).start()
    }

    private fun ImageView.iconAnimation(duration: Long, endAction: Runnable? = null) =
        animate()
            .setStartDelay(START_DELAY)
            .scaleX(60f)
            .scaleY(60f)
            .setDuration(duration)
            .apply {
                endAction?.let { action ->
                    withEndAction(action)
                }
            }
            .start()

}
