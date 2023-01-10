package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.UserLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = arrayListOf<User>()

    inner class UserViewHolder(val binding: UserLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserLayoutBinding.inflate(LayoutInflater.from(
            parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.binding.usernameTv.text = currentUser.name
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size

    fun setData(newUserList: ArrayList<User>){
        val diffUtil = MyDiffUtil(userList, newUserList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        userList = newUserList
        diffResults.dispatchUpdatesTo(this)
    }
}