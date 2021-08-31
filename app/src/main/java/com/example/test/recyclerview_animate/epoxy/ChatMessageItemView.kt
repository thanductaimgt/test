package com.example.test.recyclerview_animate.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.example.test.R
import com.example.test.R2

@ModelView(defaultLayout = R2.layout.live_show_chat_messages_item_view)
class ChatMessageItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val chatSenderUserNameTextView: TextView by lazy { findViewById<TextView>(R.id.chatSenderUserNameTextView) }
    private val chatMessageContentTextView: TextView by lazy { findViewById<TextView>(R.id.chatMessageContentTextView) }
    private val chatSenderMedals: TextView by lazy { findViewById<TextView>(R.id.chatSenderMedals) }

    @TextProp
    fun setSenderUserName(name: CharSequence?) {
        chatSenderUserNameTextView.isVisible = name != null
        chatSenderUserNameTextView.text = name?.takeIf { it.isNotEmpty() } ?: "guest"
    }

    @TextProp
    fun setMessageContent(content: CharSequence?) {
        chatMessageContentTextView.text = content
    }

    @ModelProp
    fun isCustomer(isCustomer: Boolean) {
        chatSenderMedals.text = isCustomer.toString()
    }
}
