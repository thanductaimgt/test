package com.example.test.tab

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.test.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tabs.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

class TabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        //

        viewPager.adapter = ViewPagerAdapter(this)
        verticalTabs.setupWithViewPager(viewPager)
        horizontalTabs.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                Log.d("tabs", "onPageSelected $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

//        viewPager2.visibility=View.VISIBLE
//        viewPager2.adapter=TabsAdapter(this)
//        HorizontalTabsMediator(horizontalTabs, viewPager2, object :HorizontalTabsMediator.TabConfigurationStrategy{
//            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                tab.text=position.toString()
//            }
//        }).attach()
//        VerticalTabsMediator(verticalTabs, viewPager2, object :VerticalTabsMediator.TabConfigurationStrategy{
//            override fun onConfigureTab(tabView: TabView, position: Int) {
//                tabView.title=ITabView.TabTitle.Builder().setContent(position.toString()).build()
//            }
//        }).attach()
//        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                Log.d("tabs", "onPageSelected $position")
//            }
//        })

        //

        verticalTabs.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView?, position: Int) {
                Log.d("verticalTabs", "onTabSelected: position: $position")
            }

            override fun onTabReselected(tab: TabView?, position: Int) {
                Log.d("verticalTabs", "onTabReselected: position: $position")
            }

            override fun onTabUnselected(tab: TabView?, position: Int) {
                Log.d("verticalTabs", "onTabUnselected: position: $position")
            }
        })

        horizontalTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                Log.d("horizontalTabs", "onTabSelected: position: $position")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                Log.d("horizontalTabs", "onTabReselected: position: $position")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                Log.d("horizontalTabs", "onTabUnselected: position: $position")
            }
        })
    }
}
