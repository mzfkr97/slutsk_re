package com.romanzhurid.re.ext

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen

fun SplashScreen.setSlideDownExitAnimation() {
    setOnExitAnimationListener { splashScreen ->
        ObjectAnimator.ofFloat(
            splashScreen.view,
            View.TRANSLATION_Y,
            0f,
            splashScreen.view.height.toFloat()
        ).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 200L

            doOnEnd { splashScreen.remove() }
        }.start()
    }
}
