package com.mypcapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mypcapp.adapter.RigVaultAdapter
import com.mypcapp.databinding.ActivityRigVaultBinding
import com.mypcapp.model.PCBuild

class RigVaultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRigVaultBinding
    private val builds = mutableListOf<PCBuild>()
    private lateinit var vaultAdapter: RigVaultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRigVaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        loadBuilds()
    }

    private fun setupRecyclerView() {
        vaultAdapter = RigVaultAdapter(builds) { build ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putParcelableArrayListExtra("selected_components", ArrayList(build.components.values.toList()))
                putExtra("budget", build.budget)
                putExtra("is_ai_build", false)
            }
            startActivity(intent)
        }
        binding.rvRigVault.layoutManager = LinearLayoutManager(this)
        binding.rvRigVault.adapter = vaultAdapter
    }

    private fun loadBuilds() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        
        binding.progressBar.visibility = View.VISIBLE
        
        FirebaseFirestore.getInstance().collection("vault")
            .whereEqualTo("userId", user.uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE
                builds.clear()
                for (doc in documents) {
                    val build = doc.toObject(PCBuild::class.java)
                    builds.add(build)
                }
                vaultAdapter.notifyDataSetChanged()
                
                if (builds.isEmpty()) {
                    binding.emptyStateLayout.visibility = View.VISIBLE
                } else {
                    binding.emptyStateLayout.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
