package com.lee.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lee.bankapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var userID = ""
    private var userName = ""
    private var balance = 0
    private var firstName = ""
    private var lastName = ""
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            validateData()
        }

        binding.loginpageButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateData() {
        userName = binding.usernameTextView.text.toString()
        balance = Integer.parseInt(binding.balanceTextNumber.text.toString())
        firstName = binding.firstnameTextView.text.toString()
        lastName = binding.lastnameTextView.text.toString()
        email = binding.emailTextView.text.toString()
        password = binding.passwordTextView.text.toString()

        if(TextUtils.isEmpty(userName)){
            binding.usernameTextView.error = "Please enter username"
        }
        else if(TextUtils.isEmpty(firstName)){
            binding.firstnameTextView.error = "Please enter first name"
        }
        else if(TextUtils.isEmpty(balance.toString())){
            binding.firstnameTextView.error = "Please enter balance"
        }
        else if(TextUtils.isEmpty(lastName)){
            binding.lastnameTextView.error = "Please enter last name"
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email entered
            binding.emailTextView.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            //no password entered
            binding.passwordTextView.error = "Please enter password"
        }
        else{
            firebaseRegister()
        }
    }

    private fun firebaseRegister() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).
        addOnSuccessListener {
            storeData()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            println("user not created")
        }
    }

    private fun storeData() {
        databaseReference = Firebase.database.reference

        val firebaseID = Firebase.auth.currentUser
        firebaseID?.let{
            userID = it.uid
        }

        val user = User(userName, balance, firstName, lastName, email)
        databaseReference.child("Users").child(userID).setValue(user)
            .addOnSuccessListener {
                binding.usernameTextView.text.clear()
                binding.balanceTextNumber.text.clear()
                binding.firstnameTextView.text.clear()
                binding.lastnameTextView.text.clear()
                binding.emailTextView.text.clear()
                binding.passwordTextView.text.clear()
            }
            .addOnFailureListener {
                println("data not saved")
            }
    }
}