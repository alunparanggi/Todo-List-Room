package com.challenge.refactory.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.challenge.refactory.R
import com.challenge.refactory.databinding.ActivityProfileBinding
import com.challenge.refactory.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        fireBaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.btnLogout.setOnClickListener {
            fireBaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        val firebaseUser = fireBaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            val email = firebaseUser.email
            binding.tvEmail.text = email
        }
    }
}