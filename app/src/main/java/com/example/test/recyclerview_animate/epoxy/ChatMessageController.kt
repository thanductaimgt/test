package com.example.test.recyclerview_animate.epoxy

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController

class ChatMessageController : EpoxyController() {
    var comments: List<Message> = ArrayList()

    override fun buildModels() {
        comments.forEach { comment ->
            chatMessageItemView {
                id("chat ${comment.commentId}")
                senderUserName(comment.userName)
                messageContent(comment.content)
                isCustomer(comment.isCustomer)
            }
        }
    }
}

data class Message(
    val commentId: String,
    val userName: String,
    val content: String
) {
    val isCustomer = userName == "Alice"
}

fun RecyclerView.requestBuildAndScrollToTop(controller: EpoxyController) {
//  val layoutManager = layoutManager as? LinearLayoutManager
//  layoutManager?.apply {
//    if (isCurrentPositionNearStart()) {
//      val modelBuildFinishedListener = object : OnModelBuildFinishedListener {
//        override fun onModelBuildFinished(result: DiffResult) {
//          controller.removeModelBuildListener(this)
//        }
//      }
//      controller.addModelBuildListener(modelBuildFinishedListener)
//    }
//  }
    controller.requestModelBuild()
}

fun RecyclerView.isCurrentPositionNearStart(itemCountThreshold: Int = 0): Boolean {
    return (layoutManager as? LinearLayoutManager)?.run {
        when {
            stackFromEnd && findLastVisibleItemPosition() >= itemCount - 1 - itemCountThreshold -> true
            !stackFromEnd && findFirstVisibleItemPosition() <= itemCountThreshold -> true
            else -> false
        }
    } ?: false
}
