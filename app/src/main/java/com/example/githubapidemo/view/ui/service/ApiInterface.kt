
package com.example.githubapidemo.view.ui.service

import com.example.githubapidemo.model.UserFollowersModel
import com.example.githubapidemo.model.UserListModel
import com.example.githubapidemo.model.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("search/users")
    fun searchUsers(
        @Header("Authorization") authorization: String,
        @Query("q") q:String,
        @Query("per_page") per_page:Int,
        @Query("page") page:Int
    ): Call<UserListModel>

    @GET("users/{username}")
    fun getUser(
        @Header("Authorization") authorization: String,
        @Path("username") username:String,
    ): Call<UserModel>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Header("Authorization") authorization: String,
        @Path("username") username:String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Call<UserFollowersModel>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Header("Authorization") authorization: String,
        @Path("username") username:String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Call<UserFollowersModel>


}



