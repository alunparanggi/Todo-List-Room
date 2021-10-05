package com.challenge.refactory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.challenge.refactory.R
import com.challenge.refactory.databinding.ActivityLoginBinding
import com.challenge.refactory.ui.dashboard.DashboardActivity
import com.challenge.refactory.ui.profile.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "onCreate: begin google sign in")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            DashboardActivity.startActivity(this, firebaseUser.email)
//            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: google sign in intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAutWithGoogleAccount(account)
            } catch (e: Exception){
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAutWithGoogleAccount(account: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult->
                val firebaseUser = firebaseAuth.currentUser

                val uid = firebaseUser?.uid
                val email = firebaseUser?.email

                Log.d(TAG, "firebaseAutWithGoogleAccount: email = $email")
                Log.d(TAG, "firebaseAutWithGoogleAccount: uid = $uid")

                if (authResult.additionalUserInfo!!.isNewUser){
                    Toast.makeText(this, "account created \n$email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "logged in \n$email", Toast.LENGTH_SHORT).show()
                }

                DashboardActivity.startActivity(this, email)
//                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Login failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}