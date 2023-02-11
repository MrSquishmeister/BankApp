package com.lee.bankapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.lee.bankapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var userID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
    }

    @SuppressLint("SetTextI18n")
    private fun checkUser() {
        //check user logged in
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is logged in
            val firebaseID = Firebase.auth.currentUser
            firebaseID?.let{
                userID = it.uid
            }
            readData()
        }
        else{
            //user is not logged in
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(userID).get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstname").value
                binding.welcomeTextView.text = "Welcome, " + firstName
            }
        }

    }
}