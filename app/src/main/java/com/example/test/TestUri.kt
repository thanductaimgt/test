package com.example.test

import android.net.Uri

fun testUri() {
    val uriStrings = listOf(
        "https://google/com",
        "https://google/com?a=0",
        "https://google/com#123",
        "http://google/com?a=0#123"
    )

    uriStrings.forEach {
        val uri = Uri.parse(it)
        0
    }
}
