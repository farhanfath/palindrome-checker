package com.project.kmtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.kmtest.api.response.DataItem
import com.project.kmtest.databinding.ItemListBinding

class UserAdapter(
    private val onItemClick: (DataItem) -> Unit,
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<DataItem>()

    inner class UserViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {
            userFirstName.text = user.firstName
            userLastName.text = user.lastName
            userEmail.text = user.email
            Glide.with(holder.itemView.context)
                .load(user.avatar)
                .into(userImage)
        }
        holder.binding.root.setOnClickListener { onItemClick(user) }

        if (position == users.size - 1) {
            onLoadMore()
        }
    }

    override fun getItemCount() = users.size

    fun addUsers(newUsers: List<DataItem>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }
}