package com.example.test.share_count

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class ShareReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val clickedComponent: ComponentName? =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT)
            } else {
                null
            }

        clickedComponent?.let {
            Log.d("ShareReceiver", it.toString())
        }
    }
}
