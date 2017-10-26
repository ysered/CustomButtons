package com.ysered.custombuttons.utils

/**
 * Checks whether [x] and [y] is inside circle.
 */
fun isInsideCircle(x: Float, y: Float, circleCenterX: Float, circleCenterY: Float, circleRadius: Float): Boolean {
    val absX = Math.pow(Math.abs(x - circleCenterX).toDouble(), 2.0)
    val absY = Math.pow(Math.abs(y - circleCenterY).toDouble(), 2.0)
    return Math.sqrt(absX + absY) < circleRadius
}
