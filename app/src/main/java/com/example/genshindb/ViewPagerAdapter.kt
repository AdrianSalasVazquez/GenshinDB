package com.example.genshindb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    val mFragmentList = ArrayList<Pair<Fragment, String>>()

    override fun getItemCount() = mFragmentList.size

    override fun createFragment(position: Int) = mFragmentList[position].first

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(Pair(fragment, title))
    }
}