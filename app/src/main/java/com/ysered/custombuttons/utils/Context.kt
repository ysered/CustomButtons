package com.ysered.custombuttons.utils

import android.content.Context
import android.util.TypedValue


fun Context.resolveAttribute(attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}
