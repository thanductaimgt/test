package com.example.test.tab

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(tabsActivity: TabsActivity) : FragmentStateAdapter(tabsActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return TabFragment()
    }
}
