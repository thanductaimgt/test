package com.example.test.tab

import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(tabsActivity: TabsActivity) :
    FragmentPagerAdapter(tabsActivity.supportFragmentManager) {
    override fun getCount() = 3

    override fun getItem(position: Int) = TabFragment()

    override fun getPageTitle(position: Int): CharSequence? = position.toString()
}
