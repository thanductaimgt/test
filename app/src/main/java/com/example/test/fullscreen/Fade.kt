package com.example.test.fullscreen

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isInvisible
import com.example.test.R


// Animate view's alpha, fadeIn means 0->1, otherwise 1->0
fun View.fade(
    fadeIn: Boolean,
    duration: Long = 300,
    animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null
) {
    getFadeAnim(duration, animatorUpdateListener).apply {
        if (fadeIn)
            start()
        else
            reverse()
    }
}

fun View.getFadeAnim(
    duration: Long,
    animatorUpdateListener: ValueAnimator.AnimatorUpdateListener?
): ObjectAnimator {
    return (getTag(R.id.tagAnimObject) as? ObjectAnimator) ?: ObjectAnimator().apply {
        interpolator = LinearInterpolator()
        target = this@getFadeAnim
        setPropertyName("alpha")
        setFloatValues(0f, 1f)
        addListener(object : SimpleAnimatorListener {
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                super.onAnimationStart(animation, isReverse)
                isInvisible = false
            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                isInvisible = isReverse
            }
        })

        setTag(R.id.tagAnimObject, this)
    }.apply {
        this.duration = duration
        this.removeAllUpdateListeners()
        animatorUpdateListener?.let { addUpdateListener(it) }
    }
}
