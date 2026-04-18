package com.mypcapp.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mypcapp.data.AiBudgetBuilder
import com.mypcapp.databinding.ActivityBudgetInputBinding
import com.mypcapp.model.PCComponent

class BudgetInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetInputBinding
    private var selectedComponents: ArrayList<PCComponent> = arrayListOf()
    private var aiBuild: List<PCComponent> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedComponents = intent.getParcelableArrayListExtra("selected_components") ?: arrayListOf()

        // Display user selections if they exist
        if (selectedComponents.isNotEmpty()) {
            binding.layoutUserSelection.visibility = View.VISIBLE
            val total = selectedComponents.sumOf { it.price }
            binding.tvUserSelectionCost.text = "₹${String.format("%,.0f", total)} (${selectedComponents.size} components)"
            
            // Adjust default budget to be at least the current selection
            if (total > 50000) {
                binding.etBudget.setText(total.toLong().toString())
                binding.budgetSeekBar.progress = total.toInt()
            }
        }

        // Setup budget input with quick select
        binding.btnGetAISuggestions.setOnClickListener {
            val budgetText = binding.etBudget.text.toString().trim()
            if (budgetText.isEmpty()) {
                Toast.makeText(this, "Please enter your budget", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val budget = budgetText.replace(",", "").replace("₹", "").toDoubleOrNull() ?: 0.0
            if (budget < 10000) {
                Toast.makeText(this, "Minimum budget is ₹10,000", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startAiBuild(budget)
        }

        binding.budgetSeekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.etBudget.setText(progress.toString())
                }
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // Quick-select chips
        binding.chip30k.setOnClickListener { binding.etBudget.setText("30000") }
        binding.chip50k.setOnClickListener { binding.etBudget.setText("50000") }
        binding.chip75k.setOnClickListener { binding.etBudget.setText("75000") }
        binding.chip100k.setOnClickListener { binding.etBudget.setText("100000") }
    }

    private fun startAiBuild(budget: Double) {
        // Show loading state
        binding.btnGetAISuggestions.isEnabled = false
        binding.btnViewAiBuild.visibility = View.GONE
        binding.layoutAiLoading.visibility = View.VISIBLE
        binding.progressAiBuild.visibility = View.VISIBLE
        binding.tvAiStatus.text = "🤖 AI is analyzing components..."

        // Animate progress bar
        val progressAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 2200
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { binding.progressAiBuild.progress = it.animatedValue as Int }
        }
        progressAnimator.start()

        // Step-by-step status messages
        val steps = listOf(
            400L  to "🔍 Scanning motherboard options...",
            800L  to "💻 Selecting optimal CPU...",
            1100L to "🎮 Picking best GPU in budget...",
            1400L to "⚡ Configuring PSU & storage...",
            1700L to "💾 Optimizing RAM & cabinet...",
            2000L to "✅ Finalizing your build..."
        )
        steps.forEach { (delay, msg) ->
            Handler(Looper.getMainLooper()).postDelayed({ binding.tvAiStatus.text = msg }, delay)
        }

        // Run AI build after animations
        Handler(Looper.getMainLooper()).postDelayed({
            aiBuild = AiBudgetBuilder.buildPc(budget, selectedComponents)
            val total = aiBuild.sumOf { it.price }

            binding.layoutAiLoading.visibility = View.GONE
            binding.btnGetAISuggestions.isEnabled = true
            binding.progressAiBuild.visibility = View.GONE

            // Show result card
            binding.layoutAiResult.visibility = View.VISIBLE
            binding.tvAiBuildTotal.text = "₹${String.format("%,.0f", total)} / ₹${String.format("%,.0f", budget)}"
            binding.tvAiComponentCount.text = "${aiBuild.size} components selected • ${
                if (budget > 0) String.format("%.0f", (total / budget) * 100) else "0"
            }% of budget used"
            binding.btnViewAiBuild.visibility = View.VISIBLE

            // "View AI Build" button logic
            binding.btnViewAiBuild.setOnClickListener {
                if (aiBuild.isEmpty()) {
                    Toast.makeText(this, "Build is not ready yet", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                // Ensure all components are present to avoid index errors in ResultActivity
                val finalBuild = ArrayList(aiBuild)
                
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putParcelableArrayListExtra("selected_components", finalBuild)
                    putExtra("budget", budget)
                    putExtra("is_ai_build", true)
                }
                startActivity(intent)
            }

        }, 2400)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
