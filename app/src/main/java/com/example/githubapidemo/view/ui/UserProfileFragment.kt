package com.example.githubapidemo.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.githubapidemo.R
import com.example.githubapidemo.databinding.FragmentUserBottomSheetBinding
import com.example.githubapidemo.databinding.FragmentUserProfileBinding
import com.example.githubapidemo.model.UserModel
import com.example.githubapidemo.utils.makeGone
import com.example.githubapidemo.view.ui.service.MyViewModelFactory
import com.example.githubapidemo.view.ui.service.NetworkUtil
import com.example.githubapidemo.viewmodel.SearchUserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UserProfileFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private lateinit var searchUserViewModel: SearchUserViewModel
    private var searchedUser: UserModel? = null
    var username: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            username = it.getString("username").toString()

        }
        init()
        addObservers()
        callAPI()
    }

    fun init() {
        searchUserViewModel =
            ViewModelProvider(this, MyViewModelFactory(SearchUserViewModel(activity!!))).get(
                SearchUserViewModel::class.java
            )

    }

    private fun callAPI() {
        if (!NetworkUtil.isInternetAvailable(activity!!)) {
            Toast.makeText(activity, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                .show()


            return
        } else {
            searchUserViewModel.getUser(q = username)
        }
    }

    fun addObservers() {

        searchUserViewModel.isLoading!!.observe(viewLifecycleOwner, Observer {


        })

        searchUserViewModel.responseError!!.observe(viewLifecycleOwner, Observer {

        })

        searchUserViewModel.userModel!!.observe(viewLifecycleOwner, Observer {
            searchedUser = it!!

            setView()

        })

    }

    private fun setView() {

        searchedUser?.let {

            _binding!!.tvUsername.text = it.login
            _binding!!.tvFollowersCount.text = it.followers.toString()
            _binding!!.tvFollowingCount.text = it.following.toString()
            _binding!!.tvCompanyName.text = it.company
            _binding!!.tvUsername.text = it.login

            _binding!!.tvFullName.text = it.name

            if(it.bio.isNullOrEmpty()){
                _binding!!.tvDescription.makeGone()
            }else{
                _binding!!.tvDescription.text =
                    it.bio.toString()
            }
            if(it.email.isNullOrEmpty()){
                _binding!!.tvEmail.makeGone()
            }else{
                _binding!!.tvEmail.text = it.email

            }
            if(it.blog.isEmpty()){
                _binding!!.tvBlog.makeGone()
            }else{
                _binding!!.tvBlog.text = it.blog

            }
            Glide.with(activity!!).load(it.avatar_url).into(
                _binding!!.profileImage
            )


        }


    }


}