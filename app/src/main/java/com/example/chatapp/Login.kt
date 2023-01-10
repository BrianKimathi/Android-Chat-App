package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()

        binding.goToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener { loginUser() }

    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun loginUser() {
        val email = binding.emailLoginEt.text.toString()
        val password = binding.passwordLoginEt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withContext(Dispatchers.Main){
                        binding.progressBarLogin.visibility = View.VISIBLE
                    }
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        binding.progressBarLogin.visibility = View.GONE
                        checkLoggedInState()
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Login, "Error ${e.message} occurred.", Toast.LENGTH_SHORT).show()
                        binding.progressBarLogin.visibility = View.GONE
                    }
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