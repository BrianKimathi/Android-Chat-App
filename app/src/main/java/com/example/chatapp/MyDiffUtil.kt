package com.example.chatapp

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil(private val oldList: ArrayList<User>, private val newList: ArrayList<User>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].uid == newList[newItemPosition].uid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].uid != newList[newItemPosition].uid -> {
                false
            }

            oldList[oldItemPosition].email != newList[newItemPosition].email -> {
                false
            }

            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }

            else -> true
        }
    }


}