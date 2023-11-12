package com.example.androidproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidproject.fragments.DashboardFragment
import com.example.androidproject.fragments.InfoFragment
import com.example.androidproject.fragments.WalletsFragment

class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {return DashboardFragment()}
            1 -> {return WalletsFragment()}
            2 -> {return InfoFragment()}
        }
        return DashboardFragment()
    }
}