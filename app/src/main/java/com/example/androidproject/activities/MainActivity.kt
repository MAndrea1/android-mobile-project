package com.example.androidproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.androidproject.R
import com.example.androidproject.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private val adapter by lazy { ViewPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        viewPager2.adapter = adapter

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position) {
                    0 -> { tab.text = getString(R.string.tab_label_dashboards)}
                    1 -> { tab.text = getString(R.string.tab_label_wallets)}
                    2 -> { tab.text = getString(R.string.tab_label_info)}
                }
            })
        tabLayoutMediator.attach()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> supportActionBar?.title = getString(R.string.tab_label_dashboards)
                    1 -> supportActionBar?.title = getString(R.string.tab_label_wallets)
                    2 -> supportActionBar?.title = getString(R.string.tab_label_info)
                }
                invalidateOptionsMenu() // Invalidate the options menu to recreate it
            }
        })
    }
}