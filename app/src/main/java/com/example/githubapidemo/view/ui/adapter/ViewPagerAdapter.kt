package com.example.githubapidemo.view.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubapidemo.view.ui.FollowersFragment
import com.example.githubapidemo.view.ui.FollowingFragment


private const val NUM_TABS = 2

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,var username:String) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FollowersFragment.newInstance(username)
            1 -> return FollowingFragment.newInstance(username)
        }
        return FollowingFragment()
    }
}
