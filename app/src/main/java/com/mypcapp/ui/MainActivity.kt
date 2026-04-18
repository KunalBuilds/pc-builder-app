package com.mypcapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mypcapp.R
import com.mypcapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setSupportActionBar(binding.toolbar)

        // Hero branding section animation
        binding.heroLayout.alpha = 0f
        binding.heroLayout.translationY = 50f
        binding.heroLayout.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(200)
            .start()

        // Core actions animation
        val actionCards = listOf(binding.btnMakeNewPC, binding.btnComparePC, binding.btnHelp)
        actionCards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationX = 100f
            card.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(600)
                .setStartDelay(400 + (index * 150L))
                .start()
        }

        binding.btnMakeNewPC.setOnClickListener {
            startActivity(Intent(this, MotherboardPickerActivity::class.java))
        }

        binding.btnComparePC.setOnClickListener {
            startActivity(Intent(this, BenchmarkActivity::class.java))
        }

        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        binding.btnViewStats.setOnClickListener {
            startActivity(Intent(this, BenchmarkActivity::class.java))
        }

        binding.btnRigHistory.setOnClickListener {
            startActivity(Intent(this, RigVaultActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
