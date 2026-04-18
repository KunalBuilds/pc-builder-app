package com.mypcapp.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mypcapp.databinding.ActivityBenchmarkBinding
import com.mypcapp.network.GeminiRepository
import kotlinx.coroutines.launch

class BenchmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBenchmarkBinding

    // Store current specs so upgrade suggester can reuse them
    private var currentCpu = ""
    private var currentGpu = ""
    private var currentRam = ""
    private var currentSsd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBenchmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(null) // Toolbar in XML is a LinearLayout, not a real Toolbar widget
        // If you want a real Toolbar, change the XML root for the top bar to androidx.appcompat.widget.Toolbar

        binding.btnAnalyze.setOnClickListener { startBenchmark() }
        // Note: Upgrade budget input is not in current XML, we might need to add it later
        // binding.btnGetUpgrade.setOnClickListener { fetchUpgradeSuggestion() }
    }

    // ── Step 1: validate inputs & call Gemini benchmark ──────────────────────

    private fun startBenchmark() {
        val cpu = binding.etCPU.text?.toString()?.trim() ?: ""
        val gpu = binding.etGPU.text?.toString()?.trim() ?: ""
        val ram = binding.etRAM.text?.toString()?.trim() ?: ""
        val ssd = binding.etStorage.text?.toString()?.trim() ?: ""

        if (cpu.isBlank() || gpu.isBlank() || ram.isBlank()) {
            Toast.makeText(this, "Please enter at least CPU, GPU and RAM", Toast.LENGTH_SHORT).show()
            return
        }

        // Save for upgrade use
        currentCpu = cpu
        currentGpu = gpu
        currentRam = ram
        currentSsd = ssd

        showLoading(true)
        binding.layoutResults.visibility = View.GONE

        lifecycleScope.launch {
            animateLoadingMessages()
            val result = GeminiRepository.benchmarkRig(
                cpu = cpu, gpu = gpu, ram = ram,
                ssd = ssd.ifBlank { "Any SSD" },
                psu = "Any PSU",
                motherboard = "Any Motherboard"
            )
            showLoading(false)
            displayResults(result)
        }
    }

    // ── Animated loading status messages ─────────────────────────────────────

    private suspend fun animateLoadingMessages() {
        val steps = listOf(
            "🔍 Reading your CPU specs...",
            "🎮 Analyzing GPU performance...",
            "🧠 Calculating bottlenecks...",
            "📊 Predicting FPS scores...",
            "✅ Compiling AI report..."
        )
        for (msg in steps) {
            binding.tvStatus.text = msg
            kotlinx.coroutines.delay(600)
        }
    }

    // ── Display benchmark results ─────────────────────────────────────────────

    private fun displayResults(result: GeminiRepository.BenchmarkResult) {
        binding.layoutResults.visibility = View.VISIBLE

        // Scores
        binding.tvRigPotential.text     = "Rig Potential: ${result.overallScore}"
        // Note: The layout doesn't have gaming/productivity score views, using AI insight
        binding.tvAiInsight.text = result.insight

        // Update stability and thermal (mocked or from result if added)
        binding.tvStability.text = "98%"
        binding.tvThermal.text = "65°C"

        // AAA FPS
        binding.tvCyberpunkFps.text = result.fps4k
        binding.tvStarfieldFps.text = result.fps1440
        
        // Stats
        binding.tvPowerDraw.text = "450W"
        binding.tvVRAM.text = "12GB"
        binding.tvEfficiency.text = "92%"
    }

    // ── Step 2: Upgrade suggestion ────────────────────────────────────────────

    private fun fetchUpgradeSuggestion() {
        // Budget input is missing in current XML, so we skip or use default
        val budget = "50000"
        
        // Show upgrade layout if we had one
        // binding.layoutUpgrades.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            val suggestion = GeminiRepository.suggestUpgrade(
                cpu           = currentCpu,
                gpu           = currentGpu,
                ram           = currentRam,
                ssd           = currentSsd.ifBlank { "Any SSD" },
                upgradeBudget = budget
            )
            binding.tvUpgrades.text = suggestion
            binding.layoutUpgrades.visibility = View.VISIBLE
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility  = if (show) View.VISIBLE else View.GONE
        binding.btnAnalyze.isEnabled      = !show
        binding.btnAnalyze.alpha          = if (show) 0.6f else 1f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
