package com.ysered.custombuttons.utils.extensions

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View


/**
 * Animates change from one from one color to another. Uses [ValueAnimator] and [ArgbEvaluator] to
 * interpolate color during [duration] milliseconds.
 *
 * [colorFrom] initial color
 * [colorTo] final color
 * [onColorChange] callback to be called when new color value will be available during animation updates
 * [interpolator] animator interpolator
 * [duration] animation duration
 */
inline fun View.animateColorChange(colorFrom: Int,
                                   colorTo: Int,
                                   interpolator: TimeInterpolator,
                                   duration: Long,
                                   crossinline onColorChange: (color: Int) -> Unit) {
    ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
        this.interpolator = interpolator
        this.duration = duration
        addUpdateListener {
            val colorValue = animatedValue as Int
            onColorChange(colorValue)
            invalidate()
        }
        start()
    }
}
