package com.elihimas.games.pastimes.views

import android.view.View

const val ANIMATION_DURATION = 600L
const val START_DELAY = 300L

private fun View.defaultAnimation()=
    animate().setDuration(ANIMATION_DURATION).setStartDelay(START_DELAY)

fun View.showWithAnimation() {
    defaultAnimation().scaleY(1f).alpha(1f).start()
}

fun View.hideWithAnimation() {
    defaultAnimation().scaleY(0f).alpha(0f).start()
}
