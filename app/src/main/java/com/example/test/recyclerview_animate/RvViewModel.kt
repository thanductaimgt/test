package com.example.test.recyclerview_animate

import androidx.lifecycle.MutableLiveData
import com.example.test.recyclerview_animate.epoxy.Message

class RvViewModel {
    val displayedMessages = MutableLiveData<List<Message>>()

    var newMessageCount = 0
}
