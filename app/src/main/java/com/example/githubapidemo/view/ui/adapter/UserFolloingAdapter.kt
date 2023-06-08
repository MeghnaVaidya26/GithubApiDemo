package com.example.githubapidemo.view.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapidemo.databinding.RowUserFollowersBinding
import com.example.githubapidemo.model.UserFollowersModel
import com.example.githubapidemo.utils.setOnMyClickListener
import com.example.githubapidemo.view.ui.listeners.OnListClickListener

class UserFolloingAdapter(
    var context: Context, var list: UserFollowersModel,
    var onListClickListener: OnListClickListener
) : RecyclerView.Adapter<UserFolloingAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RowUserFollowersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowUserFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                holder.binding.tvUsername.text = this.login
                Glide.with(context).load(this.avatar_url).into(holder.binding.profileImage)

            }

            holder.itemView.setOnMyClickListener {
                onListClickListener.onListClick(position, list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}