package com.example.test.fullscreen

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import vn.tiki.android.dls.extension.dp
import java.lang.ref.WeakReference
import java.util.*

interface KeyboardVisibilityChangeListener {
    fun onKeyboardVisibilityChanged(isShow: Boolean, keyboardHeight: Int, viewHeight: Int)
}

class KeyboardVisibilityChangeEmitter {
    private val mVisibleViewArea: Rect by lazy { Rect() }
    private var keyboardHeight: Int = 0
    private val mMinKeyboardHeightDetected = 60.dp
    private var rootViewRef: WeakReference<View>? = null
    private val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            val rootView = rootViewRef?.get() ?: return@OnGlobalLayoutListener
            rootView.getWindowVisibleDisplayFrame(mVisibleViewArea)
            val heightDiff =
                rootView.resources.displayMetrics.heightPixels - mVisibleViewArea.bottom

            val isKeyboardShowingOrKeyboardHeightChanged =
                keyboardHeight != heightDiff && heightDiff > mMinKeyboardHeightDetected
            if (isKeyboardShowingOrKeyboardHeightChanged) {
                keyboardHeight = heightDiff
                listeners.keys.forEach {
                    it.onKeyboardVisibilityChanged(
                        true,
                        keyboardHeight,
                        mVisibleViewArea.height()
                    )
                }
                return@OnGlobalLayoutListener
            }

            val isKeyboardHidden = keyboardHeight != 0 && heightDiff <= mMinKeyboardHeightDetected
            if (isKeyboardHidden) {
                keyboardHeight = 0
                listeners.keys.forEach {
                    it.onKeyboardVisibilityChanged(
                        false,
                        keyboardHeight,
                        mVisibleViewArea.height()
                    )
                }
            }
        }
    }

    private val onRootViewAttachStateChangeListener: View.OnAttachStateChangeListener by lazy {
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                rootViewRef = WeakReference(view)
                view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
            }

            override fun onViewDetachedFromWindow(view: View) {
                view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            }
        }
    }

    private val listeners: WeakHashMap<KeyboardVisibilityChangeListener, Any?> = WeakHashMap()

    fun setupView(view: View) {
        view.rootView.addOnAttachStateChangeListener(onRootViewAttachStateChangeListener)
    }

    fun registerListener(listener: KeyboardVisibilityChangeListener) {
        listeners[listener] = null
    }

    fun unregisterListener(listener: KeyboardVisibilityChangeListener) {
        listeners.remove(listener)
    }
}
