package com.ysered.custombuttons.utils

/**
 * Checks whether [x] and [y] is inside circle.
 */
fun isInsideCircle(x: Float, y: Float, circleX: Float, circleY: Float, circleRadius: Float): Boolean {
    val absX = Math.pow(Math.abs(x - circleX).toDouble(), 2.0)
    val absY = Math.pow(Math.abs(y - circleY).toDouble(), 2.0)
    return Math.sqrt(absX + absY) < circleRadius
}
