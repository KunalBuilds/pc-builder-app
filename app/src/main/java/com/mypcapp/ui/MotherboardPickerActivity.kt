package com.mypcapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.mypcapp.databinding.ActivityMotherboardPickerBinding
import com.mypcapp.model.ComponentCategory

class MotherboardPickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMotherboardPickerBinding
    private val viewModel: ArchitectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherboardPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reset selection on entry
        updateSelectionUI()

        binding.btnIntel.setOnClickListener {
            viewModel.selectedBrand = "Intel"
            updateSelectionUI()
        }

        binding.btnAMD.setOnClickListener {
            viewModel.selectedBrand = "AMD"
            updateSelectionUI()
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.selectedBrand.isEmpty()) {
                Toast.makeText(this, "Please select an architecture", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            navigateToComponents(viewModel.selectedBrand)
        }
    }

    private fun updateSelectionUI() {
        val isIntel = viewModel.selectedBrand == "Intel"
        val isAmd = viewModel.selectedBrand == "AMD"
        
        // Use elevation and background color for selection feedback on standard CardView
        binding.btnIntel.cardElevation = if (isIntel) 12f else 2f
        binding.btnIntel.setCardBackgroundColor(getColor(if (isIntel) com.mypcapp.R.color.surface_container_highest else com.mypcapp.R.color.surface_container_lowest))
        binding.intelSelectedBadge.visibility = if (isIntel) View.VISIBLE else View.GONE

        binding.btnAMD.cardElevation = if (isAmd) 12f else 2f
        binding.btnAMD.setCardBackgroundColor(getColor(if (isAmd) com.mypcapp.R.color.surface_container_highest else com.mypcapp.R.color.surface_container_low))
    }

    private fun navigateToComponents(brand: String) {
        val intent = Intent(this, ComponentSelectionActivity::class.java).apply {
            putExtra(ComponentSelectionActivity.EXTRA_SELECTED_BRAND, brand)
            putExtra(ComponentSelectionActivity.EXTRA_START_CATEGORY,
                ComponentCategory.MOTHERBOARD.name)
        }
        startActivity(intent)
    }
}
