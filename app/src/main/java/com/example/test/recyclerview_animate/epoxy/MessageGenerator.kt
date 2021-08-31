package com.example.test.recyclerview_animate.epoxy

import android.os.Handler
import android.os.Looper
import kotlin.random.Random

class MessageGenerator {
    var interval = 500L
    var onNewMessagesGenerated: ((newMessages: List<Message>) -> Unit)? = null
    private val handler = Handler(Looper.getMainLooper())
    private val postMessagesRunnable = object : Runnable {
        override fun run() {
            if (isCanceled) return
            generateNewMessages()
            handler.postDelayed(this, interval)
        }
    }

    private var isCanceled = false
    private var incrementalCommentId = 0

    fun startPostMessages() {
        isCanceled = false
        postMessagesRunnable.run()
    }

    fun stopPostMessages() {
        isCanceled = true
        handler.removeCallbacks(postMessagesRunnable)
    }

    private fun generateNewMessages() {
        val newMessages = arrayListOf<Message>().apply {
            val end = Random.nextInt(0, 3)
            for (i in 0..end) {
                add(
                    Message(
                        commentId = incrementalCommentId++.toString(),
                        userName = if (incrementalCommentId % 2 == 0) "Bob" else "Alice",
                        content = if (incrementalCommentId % 2 == 0) "Hello\nNice to meet you" else "Hai !"
                    )
                )
            }
        }
        onNewMessagesGenerated?.invoke(newMessages)
    }
}
