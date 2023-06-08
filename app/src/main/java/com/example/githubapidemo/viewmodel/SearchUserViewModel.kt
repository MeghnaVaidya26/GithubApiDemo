package com.example.githubapidemo.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapidemo.model.UserFollowersModel
import com.example.githubapidemo.model.UserListModel
import com.example.githubapidemo.model.UserModel
import com.example.githubapidemo.utils.Constant
import com.example.githubapidemo.view.ui.service.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel(activity: Activity) : ViewModel() {
    var context = activity
    var userListModel: MutableLiveData<UserListModel>? = MutableLiveData()
    var isLoading: MutableLiveData<Boolean>? = MutableLiveData()
    var responseError: MutableLiveData<ResponseBody>? = MutableLiveData()
    var userModel: MutableLiveData<UserModel>? = MutableLiveData()
    var userFollowerModel: MutableLiveData<UserFollowersModel>? = MutableLiveData()
    var userFollowingModel: MutableLiveData<UserFollowersModel>? = MutableLiveData()

    fun getUserSearch(q: String, page: Int) {
        isLoading?.value = true
        ApiClient.getClient(context)
            .searchUsers(authorization = Constant.apiKey, q = q, per_page = 10, page = page)
            .enqueue(object : Callback<UserListModel> {
                override fun onResponse(
                    call: Call<UserListModel>, response: Response<UserListModel>
                ) {
                    isLoading?.value = false
                    if (response.isSuccessful) {

                        if (page == 1) {
                            userListModel?.value = response.body().apply {
                                this?.paginationEnded =
                                    response.body()?.items!!.size >= Constant.paginationLimit
                            }
                        } else {
                            userListModel?.value =
                                userListModel?.value?.apply {
                                    this.items?.addAll(response.body()!!.items!!)
                                    this.paginationEnded =
                                        response.body()?.items!!.size >= Constant.paginationLimit
                                }
                        }
                    } else {
                        responseError?.value = response.errorBody()

                    }
                }

                override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                    isLoading?.value = false
                }

            })
    }

    fun getUser(q: String) {
        isLoading?.value = true
        ApiClient.getClient(context).getUser(authorization = Constant.apiKey, username = q)
            .enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>, response: Response<UserModel>
                ) {
                    isLoading?.value = false
                    if (response.isSuccessful) {
                        userModel!!.value = response.body()
                    } else {
                        responseError?.value = response.errorBody()

                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    isLoading?.value = false
                }

            })
    }

    fun getUserFollowing(q: String, page: Int) {
        isLoading?.value = true
        ApiClient.getClient(context).getUserFollowing(authorization = Constant.apiKey, username = q,page=page, per_page = 10)
            .enqueue(object : Callback<UserFollowersModel> {
                override fun onResponse(
                    call: Call<UserFollowersModel>, response: Response<UserFollowersModel>
                ) {
                    isLoading?.value = false
                    if (response.isSuccessful) {
                        userFollowingModel!!.value = response.body()
                        if (page == 1) {
                            userFollowingModel?.value = response.body().apply {
                                Constant.paginationFollowingEnded =
                                    response.body()?.size!! >= Constant.paginationLimit
                            }
                        } else {
                            userFollowingModel?.value =
                                userFollowingModel?.value?.apply {
                                    this.addAll(response.body()!!)
                                    Constant.paginationFollowingEnded =
                                        response.body()?.size!! >= Constant.paginationLimit
                                }
                        }
                    } else {
                        responseError?.value = response.errorBody()

                    }
                }

                override fun onFailure(call: Call<UserFollowersModel>, t: Throwable) {
                    isLoading?.value = false
                }

            })
    }

    fun getUserFollowers(q: String, page: Int) {
        isLoading?.value = true
        ApiClient.getClient(context).getUserFollowers(
            authorization = Constant.apiKey,
            username = q,
            page = page,
            per_page = 10
        )
            .enqueue(object : Callback<UserFollowersModel> {
                override fun onResponse(
                    call: Call<UserFollowersModel>, response: Response<UserFollowersModel>
                ) {
                    isLoading?.value = false
                    if (response.isSuccessful) {
                        if (page == 1) {
                            userFollowerModel?.value = response.body().apply {
                                Constant.paginationFollowersEnded =
                                    response.body()?.size!! >= Constant.paginationLimit
                            }
                        } else {
                            userFollowerModel?.value =
                                userFollowerModel?.value?.apply {
                                    this.addAll(response.body()!!)
                                    Constant.paginationFollowersEnded =
                                        response.body()?.size!! >= Constant.paginationLimit
                                }
                        }
                    } else {
                        responseError?.value = response.errorBody()

                    }
                }

                override fun onFailure(call: Call<UserFollowersModel>, t: Throwable) {
                    isLoading?.value = false
                }

            })
    }
}