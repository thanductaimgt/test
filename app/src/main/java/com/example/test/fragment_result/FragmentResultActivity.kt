package com.example.test.fragment_result

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.test.R

class FragmentResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_result)

        val callback = object : FragmentResultFragment.Callback {
            override fun onDismiss(code: Int, intent: Intent?) {
                Toast.makeText(this@FragmentResultActivity, "onDismiss", Toast.LENGTH_SHORT).show()
            }
        }

        val fragment = FragmentResultFragment().apply {
            arguments = bundleOf(
                FragmentResultFragment.KEY_CALLBACK to callback
            )
        }

        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                fragment,
                FragmentResultFragment::class.java.name
            )
            .addToBackStack(FragmentResultFragment::class.java.name)
            .commit()
    }
}
