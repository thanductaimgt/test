package com.example.test

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


fun View.moveOnDrag(touchView: View = this, boundRect: Rect?=null){
    // touchView.setOnTouchListener(object : View.OnTouchListener {
    //   private var lastActionDownTime = System.currentTimeMillis()
    //   private var dX = 0f
    //   private var dY = 0f
    //   private var initX = 0f
    //   private var initY = 0f
    //
    //   override fun onTouch(view: View, event: MotionEvent): Boolean {
    //     val curActionTime = System.currentTimeMillis()
    //
    //     when (event.action) {
    //       MotionEvent.ACTION_DOWN -> {
    //         lastActionDownTime=curActionTime
    //         initX = this@moveOnDrag.x
    //         initY = this@moveOnDrag.y
    //         dX = this@moveOnDrag.x - event.rawX
    //         dY = this@moveOnDrag.y - event.rawY
    //       }
    //       MotionEvent.ACTION_MOVE -> this@moveOnDrag.animate()
    //         .x(event.rawX + dX)
    //         .y(event.rawY + dY)
    //         .setDuration(0)
    //         .start()
    //       MotionEvent.ACTION_UP->{
    //         val newX = this@moveOnDrag.x
    //         val newY = this@moveOnDrag.y
    //
    //         if (gap(initX, initY, newX, newY)<10) {
    //           if(curActionTime - lastActionDownTime < ViewConfiguration.getLongPressTimeout()) {
    //             touchView.performClick()
    //           }else{
    //             touchView.performLongClick()
    //           }
    //         }
    //       }
    //       else -> return false
    //     }
    //     return true
    //   }
    //
    //   private fun gap(x1:Float, y1:Float, x2:Float, y2:Float):Float{
    //     return sqrt((x1-x2).pow(2) + (y1-y2).pow(2))
    //   }
    // })

    touchView.setOnTouchListener(object : View.OnTouchListener {
        private var lastActionDownTime = System.currentTimeMillis()
        private var touchToLeft = 0f
        private var touchToTop = 0f
        private var touchToRight = 0f
        private var touchToBottom = 0f
        private var initX=0f
        private var initY=0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val curActionTime = System.currentTimeMillis()

            val eventAction = event.action
            val eX = event.x+touchView.x-x
            val eY = event.y+touchView.y-y

            when (eventAction) {
                MotionEvent.ACTION_DOWN -> {
                    lastActionDownTime = System.currentTimeMillis()
                    touchToLeft = eX
                    touchToTop = eY
                    touchToRight = eX+width
                    touchToBottom = eY+height
                    initX = touchView.x
                    initY = touchView.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = min((boundRect?.right?:0) - touchToRight, max(eX-touchToLeft, 0f))
                    val newY = min(height - touchToBottom, max(touchToTop, eY))

                    x = newX - touchToLeft
                    y = newY - touchToTop

                    touchToLeft = eX - x
                    touchToTop = eY - y
                    touchToRight = x+width - eX
                    touchToBottom = y+height - eY
                }
                MotionEvent.ACTION_UP -> {
                    val newX = touchView.x
                    val newY = touchView.y

                    if (gap(initX, initY, newX, newY) < 10) {
                        if (curActionTime - lastActionDownTime < ViewConfiguration.getLongPressTimeout()) {
                            touchView.performClick()
                        } else {
                            touchView.performLongClick()
                        }
                    }
                }
            }
            return true
        }

        private fun gap(x1:Float, y1:Float, x2:Float, y2:Float):Float{
            return sqrt((x1-x2).pow(2) + (y1-y2).pow(2))
        }
    })
}