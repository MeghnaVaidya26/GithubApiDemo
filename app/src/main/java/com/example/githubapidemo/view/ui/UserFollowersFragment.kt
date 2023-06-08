package com.example.githubapidemo.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.githubapidemo.R
import com.example.githubapidemo.databinding.FragmentFollowersBinding
import com.example.githubapidemo.databinding.FragmentUserFollowersBinding
import com.example.githubapidemo.view.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class UserFollowingFragment : Fragment() {
    private var _binding: FragmentUserFollowersBinding? = null
    val fragmentArray = arrayOf(
        "Followers",
        "Following",
    )
    var username = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            username = it.getString("username").toString()
        }
        _binding!!.toolbar.tvTitle.text = username
        val adapter = ViewPagerAdapter(activity!!.supportFragmentManager, lifecycle, username)
        _binding!!.viewPager.adapter = adapter

        TabLayoutMediator(_binding!!.tbLayout, _binding!!.viewPager) { tab, position ->
            tab.text = fragmentArray[position]
        }.attach()

_binding!!.toolbar.ivLeftArro.setOnClickListener {
    findNavController().popBackStack()
}
    }
}