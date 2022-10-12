package com.example.test

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import kotlin.math.roundToInt

val Number.dp: Int
    get() = (toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()

fun View.updateMargin(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    if (marginLeft == left &&
        marginTop == top &&
        marginRight == right &&
        marginBottom == bottom) {
        return
    }

    layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        updateMargins(left, top, right, bottom)
    }
}