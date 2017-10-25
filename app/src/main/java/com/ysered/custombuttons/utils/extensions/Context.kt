package com.ysered.custombuttons.utils.extensions

import android.content.Context
import android.util.TypedValue


fun Context.resolveResource(attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}


fun Context.toPx(dp: Int): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
