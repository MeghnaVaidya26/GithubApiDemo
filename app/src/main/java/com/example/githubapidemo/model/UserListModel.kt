package com.example.githubapidemo.model

import com.google.gson.annotations.SerializedName

data class UserListModel(
    @SerializedName("incomplete_results")

    val incomplete_results: Boolean,
    @SerializedName("items")

    val items: ArrayList<Item>,
    @SerializedName("total_count")

    val total_count: Int,
    var paginationEnded: Boolean = false

)