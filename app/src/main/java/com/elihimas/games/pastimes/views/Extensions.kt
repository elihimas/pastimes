package com.elihimas.games.pastimes.views

import android.view.View

const val ANIMATION_DURATION = 600L

fun View.showWithAnimation() {
    animate().setDuration(ANIMATION_DURATION).scaleY(1f).alpha(1f)
}

fun View.hideWithAnimation() {
    animate().setDuration(ANIMATION_DURATION).scaleY(0f).alpha(0f)
}
