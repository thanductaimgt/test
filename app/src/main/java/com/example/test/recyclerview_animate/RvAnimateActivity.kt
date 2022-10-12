package com.example.test.recyclerview_animate

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnRepeat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.dp
import com.example.test.recyclerview_animate.epoxy.*
import kotlinx.android.synthetic.main.activity_rv_animate.*
//import vn.tiki.android.dls.extension.dp


class RvAnimateActivity : AppCompatActivity() {
    private lateinit var controller: ChatMessageController
    private var dropOldMessagesEnabled = true
    private val messageGenerator = MessageGenerator()

    private val handler = Handler(Looper.getMainLooper())
    private var lastUpdateTime = -1L
    private var hasPendingUpdate = false

    private lateinit var valueAnimator: ValueAnimator
    private val interpolator = LinearInterpolator()

    private val rvViewModel = RvViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_animate)

        controller = ChatMessageController()
        valueAnimator = ValueAnimator().apply {
            setFloatValues(0f, 1f)
            duration = SCROLL_DURATION
            repeatCount = ValueAnimator.INFINITE
            interpolator = this@RvAnimateActivity.interpolator
            doOnRepeat {
//                recyclerView.smoothScrollBy(
//                    0,
//                    40.dp,
//                    this@RvAnimateActivity.interpolator,
//                    SCROLL_DURATION.toInt()
//                )
                Log.d("RvActivity", "scroll")
            }
        }

        recyclerView.apply {
            setController(controller)
            setItemSpacingDp(10)
            (layoutManager as? LinearLayoutManager)?.stackFromEnd = true
            (layoutManager as? LinearLayoutManager)?.orientation = RecyclerView.VERTICAL

            valueAnimator.start()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && isCurrentPositionNearStart()) {
                        enableDropOldMessages()
                        valueAnimator.start()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy < 0) {
                        valueAnimator.cancel()
                        disableDropOldMessages()
                    }
                }
            })
            post {
                updateLayoutParams {
                    val screenHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        windowManager.currentWindowMetrics.bounds.height()
                    } else {
                        val metrics = DisplayMetrics()
                        windowManager.defaultDisplay.getMetrics(metrics)
                        metrics.heightPixels
                    }
                    height = screenHeight / 2 - 48.dp
                }
            }
        }

        updateDelayText()
        updateIntervalText()
        updateScrollDurationText()

        startButton.setOnClickListener {
            messageGenerator.startPostMessages()
        }

        stopButton.setOnClickListener {
            messageGenerator.stopPostMessages()
        }

        delayPlusButton.setOnClickListener {
            messageGenerator.interval += 100
            updateDelayText()
        }

        delaySubButton.setOnClickListener {
            messageGenerator.interval = (messageGenerator.interval - 100).coerceAtLeast(0)
            updateDelayText()
        }

        updatePlusButton.setOnClickListener {
            CHAT_UPDATE_INTERVAL += 100
            updateIntervalText()
        }

        updateSubButton.setOnClickListener {
            CHAT_UPDATE_INTERVAL = (CHAT_UPDATE_INTERVAL - 100).coerceAtLeast(0)
            updateIntervalText()
        }

        clearButton.setOnClickListener {
            controller.comments = arrayListOf()
            recyclerView.requestBuildAndScrollToTop(controller)
        }

        scrollPlusButton.setOnClickListener {
            SCROLL_DURATION += 50
            applyScrollDuration()
        }

        scrollSubButton.setOnClickListener {
            SCROLL_DURATION = (SCROLL_DURATION - 50).coerceAtLeast(0)
            applyScrollDuration()
        }

        scrollButton.setOnClickListener {
            valueAnimator.start()
        }

        messageGenerator.onNewMessagesGenerated = onNewMessagesGenerated@{
            rvViewModel.displayedMessages.value = arrayListOf<Message>().apply {
                addAll(controller.comments)
                addAll(it)
            }.let {
                if (dropOldMessagesEnabled) {
                    it.takeLast(FIXED_MESSAGE_COUNT).toMutableList() as ArrayList<Message>
                } else {
                    it
                }
            }

            rvViewModel.newMessageCount += it.size
        }

        rvViewModel.displayedMessages.observe(this, Observer {
            if (hasPendingUpdate) return@Observer

            val timeGap = System.currentTimeMillis() - lastUpdateTime
            if (timeGap >= CHAT_UPDATE_INTERVAL) {
                updateChat()
            } else {
                delayUpdateChat(CHAT_UPDATE_INTERVAL - timeGap)
            }
        })
    }

    private fun applyScrollDuration() {
        valueAnimator.duration = SCROLL_DURATION
        updateScrollDurationText()
    }

    private fun updateScrollDurationText() {
        scrollDurationTextView.text = "duration: $SCROLL_DURATION"
    }

    private fun updateIntervalText() {
        intervalTextView.text = "interval: $CHAT_UPDATE_INTERVAL"
    }

    private fun updateChat() {
        controller.comments = rvViewModel.displayedMessages.value!!
        recyclerView.requestBuildAndScrollToTop(controller)
        val currentTime = System.currentTimeMillis()
        val avgMessageInterval =
            if (rvViewModel.newMessageCount > 0) (currentTime - lastUpdateTime) / rvViewModel.newMessageCount else 0

        val newScrollDuration = avgMessageInterval.toClosestValidScrollDuration()
        CHAT_UPDATE_INTERVAL = newScrollDuration.toInt()
        updateIntervalText()
        SCROLL_DURATION = newScrollDuration
        applyScrollDuration()

        lastUpdateTime = currentTime
        rvViewModel.newMessageCount = 0
    }

    private fun Long.toClosestValidScrollDuration(): Long {
        return when {
            this > 350 -> 350
            this > 250 -> 250
            else -> 150
        }
    }

    private fun delayUpdateChat(delay: Long) {
        hasPendingUpdate = true
        handler.postDelayed({
            updateChat()
            hasPendingUpdate = false
        }, delay)
    }

    private fun updateDelayText() {
        delayTextView.text = "delay: ${messageGenerator.interval}"
    }

    private fun enableDropOldMessages() {
        dropOldMessagesEnabled = true
    }

    private fun disableDropOldMessages() {
        dropOldMessagesEnabled = false
    }

    companion object {
        private const val FIXED_MESSAGE_COUNT = 100
        private var CHAT_UPDATE_INTERVAL = 500
        private var SCROLL_DURATION = 200L
    }
}
