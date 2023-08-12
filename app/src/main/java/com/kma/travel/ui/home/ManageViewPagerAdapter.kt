package com.kma.travel.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kma.travel.ui.myfile.MyFileFragment


class ManageViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return HomeFragment()
            1 -> return MyFileFragment()
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Download"
            1 -> return "My Files"
        }
        return null!!
    }

}