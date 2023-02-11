package com.lee.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.lee.bankapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.loginButton.setOnClickListener{
            validateData()
        }

        binding.registerpageButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateData() {
        email = binding.emailTextView.text.toString()
        password = binding.passwordTextView.text.toString()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email entered
            binding.emailTextView.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            //no password entered
            binding.passwordTextView.error = "Please enter password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
            .addOnFailureListener{
                println("login not successful")
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}