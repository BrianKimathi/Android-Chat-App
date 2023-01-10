package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.RecievedLayoutBinding
import com.example.chatapp.databinding.SentLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE = 1
    val ITEM_SENT = 2

    private var messageList = arrayListOf<Message>()

    inner class SentViewHolder(val binding: SentLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ReceivedViewHolder(val binding: RecievedLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1){
            ReceivedViewHolder(RecievedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            SentViewHolder(SentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.binding.sentTextTv.text = messageList[position].message
        }else{
            val viewHolder = holder as ReceivedViewHolder
            holder.binding.receivedTextTv.text = messageList[position].message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == currentMsg.senderId){
            ITEM_SENT
        }else{
            ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int = messageList.size

    fun setMessages(newMessageList: ArrayList<Message>){
        val diffUtil = MessageDiffUtil(messageList, newMessageList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        messageList = newMessageList
        diffResults.dispatchUpdatesTo(this)
    }

}