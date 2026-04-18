package com.mypcapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mypcapp.R
import com.mypcapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var googleSignInClient: GoogleSignInClient? = null

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Initial Animations
        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(1000).start()
        
        // Form slide up animation
        binding.btnEmailLogin.parent.let { view ->
            if (view is android.view.View) {
                view.translationY = 400f
                view.animate().translationY(0f).setDuration(800).setStartDelay(300).start()
            }
        }

        // Configure Google Sign-In
        try {
            val webClientId = getString(R.string.default_web_client_id)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
        } catch (e: Exception) {
            Log.e(TAG, "Google Sign-In setup failed", e)
            binding.btnGoogleSignIn.isEnabled = false
            binding.btnGoogleSignIn.alpha = 0.5f
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnEmailLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginWithEmail(email, password)
        }

        binding.btnCreateAccount.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createAccount(email, password)
        }
    }

    private fun signInWithGoogle() {
        val client = googleSignInClient
        if (client == null) {
            Toast.makeText(this, "Google Sign-In is not configured yet. Use Email/Password login.", Toast.LENGTH_LONG).show()
            return
        }
        showProgress(true)
        val signInIntent = client.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginWithEmail(email: String, password: String) {
        // Developer hardcoded login
        if (email == "kunall31meena@gmail.com" && password == "Kun@l9560") {
            navigateToMain()
            return
        }

        showProgress(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createAccount(email: String, password: String) {
        showProgress(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showProgress(false)
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnGoogleSignIn.isEnabled = !show
        binding.btnEmailLogin.isEnabled = !show
        binding.btnEmailLogin.alpha = if (show) 0.6f else 1.0f
        binding.btnCreateAccount.isEnabled = !show
    }
}
