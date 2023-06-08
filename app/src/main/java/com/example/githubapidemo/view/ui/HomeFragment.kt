package com.example.githubapidemo.view.ui

import android.os.Bundle
import android.view.KeyEvent
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
import com.example.githubapidemo.databinding.FragmentHomeBinding
import com.example.githubapidemo.model.Item
import com.example.githubapidemo.model.UserListModel
import com.example.githubapidemo.utils.makeGone
import com.example.githubapidemo.view.ui.adapter.UserListAdapter
import com.example.githubapidemo.view.ui.base.BaseFragment
import com.example.githubapidemo.view.ui.service.MyViewModelFactory
import com.example.githubapidemo.view.ui.service.NetworkUtil
import com.example.githubapidemo.viewmodel.SearchUserViewModel
import java.lang.Exception


class HomeFragment : BaseFragment() {
    private var searchUserViewModel: SearchUserViewModel? = null
    private var _binding: FragmentHomeBinding? = null
    private var userlistModel: UserListModel? = null
    var searchUserList: ArrayList<Item> = ArrayList()
    var userListAdapter: UserListAdapter? = null
    var page: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding!!.toolbar.tvTitle.text = getString(R.string.home)
        _binding!!.toolbar.ivLeftArro.makeGone()
        init()
        onClickListeners()
        addObservers()
    }

    private fun onClickListeners() {
        _binding!!.rvusers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!_binding!!.rvusers.canScrollVertically(1) && searchUserViewModel?.isLoading?.value == false) {
                    page++
                    callAPI()
                }
            }
        })


        _binding?.edtSearch?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page = 1
                    callAPI()
                    return true
                }
                return false
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

        searchUserViewModel!!.userListModel!!.observe(viewLifecycleOwner, Observer {
            userlistModel = it
            (_binding!!.rvusers.adapter as UserListAdapter?)?.notifyDataSetChanged()
                ?: run {
                    try {
                        _binding!!.rvusers.adapter =
                            UserListAdapter(
                                activity!!,
                                it.items as ArrayList<Item>,

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
            searchUserViewModel!!.getUserSearch(
                q = _binding!!.edtSearch.text.toString(),
                page = page
            )
        }
    }

    override fun onListClick(position: Int, obj: Any?) {

        val bundle = bundleOf("username" to (obj as Item).login)

        findNavController().navigate(R.id.user_bio_fragment, bundle)

    }
}