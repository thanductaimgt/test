package com.example.test.fullscreen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.view.*
import com.example.test.R
import com.example.test.updateMargin
import kotlinx.android.synthetic.main.activity_full_screen.*
//import vn.tiki.android.dls.extension.updateMargin

class FullScreenActivity : AppCompatActivity() {
    private var isHide = false
    private lateinit var insetController: WindowInsetsControllerCompat
    private var lastImeVisible = false

    private val keyboardEventEmitter by lazy { KeyboardVisibilityChangeEmitter() }

    private val chatKeyboardListener: KeyboardVisibilityChangeListener by lazy {
        object : KeyboardVisibilityChangeListener {
            override fun onKeyboardVisibilityChanged(
                isShow: Boolean,
                keyboardHeight: Int,
                viewHeight: Int
            ) {
                Log.d(
                    "ABC",
                    "onKeyboardVisibilityChanged $isShow kH=$keyboardHeight vH=$viewHeight"
                )
                editText.updateMargin(bottom = if (isShow) keyboardHeight else 0)
                // hide input if keyboard is off by any cause
//                val showInput = withState(liveChatViewModel.value) { it.showInput }
//                if (!isShow && showInput) {
//                    liveChatViewModel.value.hideInput()
//                }
//                if(isShow) {
//                    insetController.show(WindowInsetsCompat.Type.navigationBars())
//                }else{
//                    insetController.hide(WindowInsetsCompat.Type.navigationBars())
//                }
                showHideButton.fade(isShow)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        setContentView(R.layout.activity_full_screen)

        lightDarkButton.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = PARENT_ID
        }

        keyboardEventEmitter.setupView(containerView)
        keyboardEventEmitter.registerListener(chatKeyboardListener)

        insetController = WindowInsetsControllerCompat(window, containerView)
//        insetController.isAppearanceLightNavigationBars=false

        insetController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

//        hideSystemUI()

        showHideButton.setOnClickListener {
            if (isHide) {
                showSystemUI()
            } else {
                hideSystemUI()
            }
            isHide = !isHide
        }

        lightDarkButton.setOnClickListener {
            insetController.isAppearanceLightStatusBars =
                !insetController.isAppearanceLightStatusBars
            insetController.isAppearanceLightNavigationBars =
                insetController.isAppearanceLightStatusBars
        }

        containerView.setOnApplyWindowInsetsListener { v, insets ->
            Log.d("ABC", insets.toString())
            val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets, containerView)
//            val systemInsets = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
//            Log.d("ABC", systemInsets.toString())
            val statusBarInsets = insetsCompat.getInsets(WindowInsetsCompat.Type.statusBars())
            Log.d("ABC", statusBarInsets.toString())
            val navigationBarInsets =
                insetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars())
            Log.d("ABC", navigationBarInsets.toString())

            val imeVisible = insetsCompat.isVisible(WindowInsetsCompat.Type.ime())
            Log.d("ABC", "ime visible: $imeVisible")
            val statusBarVisible = insetsCompat.isVisible(WindowInsetsCompat.Type.statusBars())
            Log.d("ABC", "statusBar visible: $statusBarVisible")
            val navigationBarVisible =
                insetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars())
            Log.d("ABC", "navigation visible: $navigationBarVisible")

            lineTop.updateMargin(top = statusBarInsets.top)
            lineBottom.updateMargin(bottom = navigationBarInsets.bottom)
            return@setOnApplyWindowInsetsListener insets
        }

        containerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.d(
                TAG,
                "OnLayoutChangeListener: $v, $left, $top, $right, $bottom, $oldLeft, $oldTop, $oldRight, $oldBottom"
            )
        }

//        containerView.setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(
//            DISPATCH_MODE_STOP
//        ) {
//            override fun onProgress(
//                insets: WindowInsets,
//                runningAnimations: MutableList<WindowInsetsAnimation>
//            ): WindowInsets {
//                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
//                val navigationBarInsets = insets.getInsets(WindowInsets.Type.navigationBars())
//                val imeInsets = insets.getInsets(WindowInsets.Type.ime())
//
////                containerView.updatePadding(top = statusBarInsets.top, bottom = navigationBarInsets.bottom)
//                editText.updateMargin(bottom = imeInsets.bottom)
//                val maxStatusInsets = insets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
//
//                Log.d("ABC", "status: $statusBarInsets")
//                Log.d("ABC", "status max: $maxStatusInsets")
//                Log.d("ABC", "naviga: $navigationBarInsets")
//                Log.d("ABC", "imeeee: $imeInsets")
//
//                val imeVisible = insets.isVisible(WindowInsets.Type.ime())
//                Log.d("ABC", "ime visible: $imeVisible")
//
//                if(imeVisible!=lastImeVisible) {
//                    if (imeVisible) {
//                        insetController.show(WindowInsetsCompat.Type.navigationBars())
//                    } else {
//                        insetController.hide(WindowInsetsCompat.Type.navigationBars())
//                    }
//                    lastImeVisible = imeVisible
//                }
//
//                return insets
//            }
//        })

        var a=0
        arrayListOf<Int>().map {
            a=1
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        insetController.apply {
//            hide(WindowInsetsCompat.Type.statusBars())
//            hide(WindowInsetsCompat.Type.navigationBars())
//            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
//        insetController.show(WindowInsetsCompat.Type.systemBars())
    }

    companion object {
        private val TAG = FullScreenActivity::class.java.simpleName
    }
}