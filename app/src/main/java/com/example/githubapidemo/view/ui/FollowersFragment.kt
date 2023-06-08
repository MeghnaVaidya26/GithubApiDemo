package com.example.githubapidemo.view.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapidemo.R
import com.example.githubapidemo.databinding.FragmentFollowersBinding
import com.example.githubapidemo.databinding.FragmentHomeBinding
import com.example.githubapidemo.model.Item
import com.example.githubapidemo.model.UserFollowersModel
import com.example.githubapidemo.model.UserFollowersModelItem
import com.example.githubapidemo.model.UserListModel
import com.example.githubapidemo.view.ui.adapter.UserFollowersAdapter
import com.example.githubapidemo.view.ui.adapter.UserListAdapter
import com.example.githubapidemo.view.ui.base.BaseFragment
import com.example.githubapidemo.view.ui.service.MyViewModelFactory
import com.example.githubapidemo.view.ui.service.NetworkUtil
import com.example.githubapidemo.viewmodel.SearchUserViewModel
import java.lang.Exception

private const val ARG_PARAM1 = "username"

class FollowersFragment : BaseFragment() {
    private var searchUserViewModel: SearchUserViewModel? = null
    private var _binding: FragmentFollowersBinding? = null
    private var followerslist: UserFollowersModel? = null
    var page: Int = 1
    var username = ""

    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_PARAM1).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        // Utils.showBottomSheetDialog(activity!!)
        onClickListeners()
        addObservers()
        callAPI()

    }

    private fun onClickListeners() {
        _binding!!.rvfollower.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!_binding!!.rvfollower.canScrollVertically(1) && searchUserViewModel?.isLoading?.value == false) {
                    page++
                    callAPI()
                }
            }
        })

        _binding!!.pulltorefresh.setOnRefreshListener {
            page = 1
            callAPI()
            _binding!!.pulltorefresh.isRefreshing = false
        }
    }

    fun init() {
        searchUserViewModel =
            ViewModelProvider(this, MyViewModelFactory(SearchUserViewModel(activity!!))).get(
                SearchUserViewModel::class.java
            )

    }

    fun addObservers() {

        searchUserViewModel!!.isLoading!!.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgressDialog()
            } else {

                dismissProgressDialog()
            }
        })

        searchUserViewModel!!.responseError!!.observe(viewLifecycleOwner, Observer {

        })

        searchUserViewModel!!.userFollowerModel!!.observe(viewLifecycleOwner, Observer {
            followerslist = it
            (_binding!!.rvfollower.adapter as UserFollowersAdapter?)?.notifyDataSetChanged()
                ?: run {
                    try {
                        _binding!!.rvfollower.adapter =
                            UserFollowersAdapter(
                                activity!!,
                                it,

                                this
                            )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

        })

    }


    private fun callAPI() {
        if (!NetworkUtil.isInternetAvailable(activity!!)) {

            shoToast(getString(R.string.no_internet_connection))

            return
        } else {
            Log.e("==username","-"+username)
            searchUserViewModel!!.getUserFollowers(q = username, page = page)
        }
    }

    override fun onListClick(position: Int, obj: Any?) {

        val bundle = bundleOf("username" to (obj as UserFollowersModelItem).login)

        findNavController().navigate(R.id.user_profile, bundle)

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }


    }

}