package com.mypcapp.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mypcapp.R
import com.mypcapp.adapter.ComponentResultAdapter
import com.mypcapp.databinding.ActivityResultBinding
import com.mypcapp.model.ComponentCategory
import com.mypcapp.model.PCBuild
import com.mypcapp.model.PCComponent
import com.mypcapp.network.GeminiRepository
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var selectedComponents: ArrayList<PCComponent> = arrayListOf()
    private var budget: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedComponents = intent.getParcelableArrayListExtra("selected_components") ?: arrayListOf()
        budget = intent.getDoubleExtra("budget", 0.0)
        val isAiBuild = intent.getBooleanExtra("is_ai_build", false)

        if (isAiBuild) {
            binding.tvBuildName.text = "🤖 AI Curated Build"
        }

        displayResults()
        animatePerformanceScores()
        provideHapticFeedback()
        checkCompatibility()

        binding.fabSaveBuild.setOnClickListener {
            saveBuildToCloud()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.result_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_build -> {
                saveBuildToCloud()
                true
            }
            R.id.action_share_build -> {
                shareBuild()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareBuild() {
        val totalCost = selectedComponents.sumOf { it.price }
        val componentList = selectedComponents.joinToString("\n") { 
            "• ${it.category.displayName}: ${it.name} (${it.brand}) — ₹${String.format("%,.0f", it.price)}"
        }
        
        val shareText = """
            🚀 My PC Build from MyPCApp:
            
            Build Name: ${binding.tvBuildName.text}
            Total Cost: ₹${String.format("%,.0f", totalCost)}
            
            Components:
            $componentList
            
            Build your own rig with MyPCApp! 🛠️
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "My PC Build - ${binding.tvBuildName.text}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share your Build via"))
    }

    private fun saveBuildToCloud() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Please log in to save builds", Toast.LENGTH_SHORT).show()
            return
        }

        val buildId = "RIG_${System.currentTimeMillis()}"
        val buildName = binding.tvBuildName.text.toString()
        val totalCost = selectedComponents.sumOf { it.price }
        
        // Convert list to map for Firestore
        val componentMap = selectedComponents.associateBy { it.category.name }

        val pcBuild = PCBuild(
            id = buildId,
            userId = user.uid,
            name = buildName,
            budget = budget,
            totalCost = totalCost,
            components = componentMap,
            createdAt = System.currentTimeMillis()
        )

        FirebaseFirestore.getInstance().collection("vault")
            .document(buildId)
            .set(pcBuild)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ Build saved to Rig Vault!", Toast.LENGTH_SHORT).show()
                binding.fabSaveBuild.text = "Saved ✅"
                binding.fabSaveBuild.isEnabled = false
                provideHapticFeedback()
                showConfetti()
            }
            .addOnFailureListener {
                Toast.makeText(this, "❌ Failed to save: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkCompatibility() {
        val cpu = selectedComponents.find { it.category == ComponentCategory.CPU }
        val mobo = selectedComponents.find { it.category == ComponentCategory.MOTHERBOARD }
        val psu = selectedComponents.find { it.category == ComponentCategory.PSU }
        val gpu = selectedComponents.find { it.category == ComponentCategory.GPU }

        val issues = mutableListOf<String>()

        // 1. Socket/Brand Check
        if (cpu != null && mobo != null) {
            if (cpu.compatibleBrand.isNotEmpty() && mobo.compatibleBrand.isNotEmpty() &&
                cpu.compatibleBrand != mobo.compatibleBrand) {
                issues.add("⚠️ CPU ($cpu.compatibleBrand) is not compatible with Motherboard ($mobo.compatibleBrand)!")
            }
        }

        // 2. Wattage Check (Simplified)
        if (psu != null) {
            val estimatedDraw = 150 + (gpu?.performanceScore ?: 0) * 2 // Dummy logic: base 150W + GPU factor
            val psuWattage = psu.specsText.filter { it.isDigit() }.toIntOrNull() ?: 500
            if (psuWattage < estimatedDraw) {
                issues.add("⚠️ PSU (${psuWattage}W) might be too weak for this high-performance build!")
            }
        }

        if (issues.isNotEmpty()) {
            binding.tvAiAnalysis.text = issues.joinToString("\n\n") + "\n\n" + binding.tvAiAnalysis.text
        }
    }

    private fun provideHapticFeedback() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }

    private fun animatePerformanceScores() {
        val totalScore = selectedComponents.sumOf { it.performanceScore }.toDouble() / (selectedComponents.size.coerceAtLeast(1))
        val targetScore = totalScore / 10.0
        
        // Animate the score text from 0.0 to target
        val animator = ValueAnimator.ofFloat(0f, targetScore.toFloat())
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            binding.tvScore.text = String.format("%.1f", value)
        }
        animator.start()
        
        // Update FPS based on final score
        val scoreFactor = totalScore / 100.0
        binding.tv4kFps.text = "${(60 + (100 * scoreFactor)).toInt()} FPS"
        binding.tv1440pFps.text = "${(120 + (200 * scoreFactor)).toInt()} FPS"
    }

    private fun displayResults() {
        val totalCost = selectedComponents.sumOf { it.price }
        val utilization = if (budget > 0) ((totalCost / budget) * 100).toInt() else 0
        val avgPerformance = if (selectedComponents.isEmpty()) 0.0 else selectedComponents.map { it.performanceScore }.average()

        // Update Stitch UI elements
        binding.tvTotalCost.text = "₹${String.format("%,.0f", totalCost)}"
        binding.tvBudgetUsed.text = "$utilization% of ₹${String.format("%,.0f", budget)}"
        binding.budgetProgressBar.progress = utilization.coerceIn(0, 100)
        binding.tvPartsCount.text = "${selectedComponents.size} PARTS SELECTED"
        binding.tvScore.text = String.format("%.1f", avgPerformance / 10.0)

        // Simple FPS estimation for UI display
        binding.tv4kFps.text = "${(avgPerformance * 1.2).toInt()} FPS"
        binding.tv1440pFps.text = "${(avgPerformance * 2.1).toInt()} FPS"

        // Components RecyclerView
        val resultAdapter = ComponentResultAdapter(selectedComponents) { component, _ ->
            val searchQuery = "${component.brand} ${component.name}"
            val encodedQuery = Uri.encode(searchQuery)
            val url = "https://www.amazon.in/s?k=$encodedQuery"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                // Fallback or error handling
            }
        }
        binding.rvResultComponents.layoutManager = LinearLayoutManager(this)
        binding.rvResultComponents.adapter = resultAdapter

        binding.progressBar.visibility = View.GONE
        fetchGeminiPerformance()
    }

    private fun fetchGeminiPerformance() {
        if (selectedComponents.isEmpty()) return

        binding.tvAiAnalysis.text = "Analyzing your build with AI…"
        binding.lottieAiThinking.visibility = View.VISIBLE

        lifecycleScope.launch {
            val analysis = GeminiRepository.predictPerformance(selectedComponents, budget)
            binding.lottieAiThinking.visibility = View.GONE
            binding.tvAiAnalysis.text = analysis

            // Check if it's a high-performance build to show confetti
            val totalScore = selectedComponents.sumOf { it.performanceScore }.toDouble() / (selectedComponents.size.coerceAtLeast(1))
            if (totalScore >= 85.0) {
                showConfetti()
            }
        }
    }

    private fun showConfetti() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        binding.konfettiView.start(party)
    }
}
