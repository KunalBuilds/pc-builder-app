package com.mypcapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mypcapp.data.ComponentDatabase
import com.mypcapp.databinding.ActivityComparePcBinding
import com.mypcapp.model.ComponentCategory
import com.mypcapp.model.PCComponent

class ComparePCActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComparePcBinding

    private val build1 = mutableMapOf<ComponentCategory, PCComponent>()
    private val build2 = mutableMapOf<ComponentCategory, PCComponent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparePcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBuildSelection()
    }

    private fun setupBuildSelection() {
        binding.tvBuild1Name.text = "Build A"
        binding.tvBuild2Name.text = "Build B"

        binding.tvBuild1Cost.text = "₹0"
        binding.tvBuild2Cost.text = "₹0"

        binding.btnConfirmBuild.setOnClickListener { compareBuilds() }

        binding.btnAIChoose.setOnClickListener {
            autoSelectBuilds()
        }
    }

    private fun autoSelectBuilds() {
        val cpus = ComponentDatabase.getComponentsByCategory(ComponentCategory.CPU)
        val gpus = ComponentDatabase.getComponentsByCategory(ComponentCategory.GPU)
        val rams = ComponentDatabase.getComponentsByCategory(ComponentCategory.RAM)

        if (cpus.isNotEmpty()) {
            build1[ComponentCategory.CPU] = cpus.first()
            build2[ComponentCategory.CPU] = cpus.last()
            binding.tvBuild1CPU.text = cpus.first().name
            binding.tvBuild2CPU.text = cpus.last().name
        }
        if (gpus.isNotEmpty()) {
            build1[ComponentCategory.GPU] = gpus.first()
            build2[ComponentCategory.GPU] = gpus.last()
            binding.tvBuild1GPU.text = gpus.first().name
            binding.tvBuild2GPU.text = gpus.last().name
        }
        if (rams.isNotEmpty()) {
            build1[ComponentCategory.RAM] = rams.first()
            build2[ComponentCategory.RAM] = rams.last()
            binding.tvBuild1RAM.text = rams.first().name
            binding.tvBuild2RAM.text = rams.last().name
        }

        // Simulate Benchmark metrics update
        binding.tvCinebench1.text = "38,500 pts"
        binding.tvCinebench2.text = "36,200 pts"
        binding.tvTimeSpy1.text = "19,400 pts"
        binding.tvTimeSpy2.text = "15,800 pts"
        binding.tvNoise1.text = "32 dBA"
        binding.tvNoise2.text = "28 dBA"
        binding.tvTdp1.text = "115W"
        binding.tvTdp2.text = "65W"

        val cost1 = build1.values.sumOf { it.price }
        val cost2 = build2.values.sumOf { it.price }
        binding.tvBuild1Cost.text = "₹${String.format("%,.0f", cost1)}"
        binding.tvBuild2Cost.text = "₹${String.format("%,.0f", cost2)}"

        Toast.makeText(this, "🤖 AI has selected the best configurations!", Toast.LENGTH_SHORT).show()
    }

    private fun compareBuilds() {
        if (build1.isEmpty() || build2.isEmpty()) {
            Toast.makeText(this, "Tap 'Let AI Choose' to load builds first", Toast.LENGTH_SHORT).show()
            return
        }

        val cost1 = build1.values.sumOf { it.price }
        val cost2 = build2.values.sumOf { it.price }
        val score1 = build1.values.sumOf { it.performanceScore }
        val score2 = build2.values.sumOf { it.performanceScore }

        val winner = if (score1 > score2) "Build A" else if (score2 > score1) "Build B" else "Tie"
        Toast.makeText(
            this,
            "🏆 Winner: $winner  |  Best Value: ₹${String.format("%,.0f", minOf(cost1, cost2))}",
            Toast.LENGTH_LONG
        ).show()
    }
}
