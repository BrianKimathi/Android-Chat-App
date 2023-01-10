package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbRef: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("name")
        supportActionBar?.title = name



        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid

        dbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid

        receiverRoom = senderUid + receiverUid

        messageAdapter = MessageAdapter(this)
        messageList = ArrayList()

        binding.chatRecycler.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }

        // ad data to recyclerview
        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (p in snapshot.children){
                        val message = p.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.setMessages(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.sendMsgBtn.setOnClickListener {
            val message = binding.messageEt.text.toString()
            val msgObj = Message(message, senderUid)

            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObj).addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(msgObj)
                }
            binding.messageEt.setText("")
        }


    }
}