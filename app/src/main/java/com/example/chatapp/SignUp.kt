package com.example.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase
    private lateinit var imageUri: Uri
    private lateinit var storage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        val getImg = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                it?.let {
                    imageUri = it
                }
                binding.userProfileImg.setImageURI(imageUri)
            }
        )

        binding.goToSignIn.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.registerBtn.setOnClickListener { registerUser() }

        binding.pickImgBtn.setOnClickListener {
            getImg.launch("image/*")
        }

    }

    private fun registerUser() {
        val email = binding.emailRegEt.text.toString()
        val password = binding.passwordRegEt.text.toString()
        val username = binding.usernameRegEt.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.VISIBLE
                }
                auth.createUserWithEmailAndPassword(email, password).await()
                dbRef.reference.child("users").child(auth.currentUser!!.uid).setValue(User(username, email, auth.currentUser!!.uid)).await()
                storage = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser!!.uid)
                storage.putFile(imageUri).await()
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    checkLoggedInState()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@SignUp, "Error ${e.message} occurred!", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}