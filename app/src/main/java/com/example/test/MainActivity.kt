package com.example.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frame1.setOnClickListener {
            it.fitsSystemWindows = it.fitsSystemWindows.not()
            Toast.makeText(this, it.fitsSystemWindows.toString(), Toast.LENGTH_SHORT).show()
        }

        frame2.setOnClickListener {
            it.fitsSystemWindows = it.fitsSystemWindows.not()
            Toast.makeText(this, it.fitsSystemWindows.toString(), Toast.LENGTH_SHORT).show()
        }

        frame3.setOnClickListener {
            it.fitsSystemWindows = it.fitsSystemWindows.not()
            Toast.makeText(this, it.fitsSystemWindows.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
