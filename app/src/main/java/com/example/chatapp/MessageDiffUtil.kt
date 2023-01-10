package com.example.chatapp

import androidx.recyclerview.widget.DiffUtil

class MessageDiffUtil(private val oldList: ArrayList<Message>, private val newList: ArrayList<Message>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].senderId == newList[newItemPosition].senderId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].senderId != newList[newItemPosition].senderId -> {
                false
            }

            oldList[oldItemPosition].message != newList[newItemPosition].message -> {
                false
            }

            else -> true
        }
    }


}