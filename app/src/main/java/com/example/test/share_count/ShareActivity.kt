package com.example.test.share_count

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    val pi = PendingIntent.getBroadcast(
                        this, SHARE_REQUEST_CODE,
                        Intent(this, ShareReceiver::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    Intent.createChooser(sendIntent, null, pi.intentSender)
                } else {
                    Intent.createChooser(sendIntent, null)
                }
            startActivity(shareIntent)
        }
    }

    companion object {
        const val SHARE_REQUEST_CODE = 1998
    }
}
