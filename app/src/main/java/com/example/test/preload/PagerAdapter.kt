package com.example.test.preload

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var showDetails = listOf<ShowDetail>()

    override fun getItemCount(): Int {
        return showDetails.size
    }

    override fun createFragment(position: Int): Fragment {
        return ShowFragment().apply {
            arguments = bundleOf(
                ShowFragment.KEY_POSITION to position
            )

        }
    }
}
