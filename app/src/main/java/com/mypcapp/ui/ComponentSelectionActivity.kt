package com.mypcapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypcapp.adapter.ComponentAdapter
import com.mypcapp.data.ComponentDatabase
import com.mypcapp.databinding.ActivityComponentSelectionBinding
import com.mypcapp.model.ComponentCategory
import java.util.Locale

class ComponentSelectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_BRAND = "extra_selected_brand"
        const val EXTRA_START_CATEGORY = "extra_start_category"
    }

    private lateinit var binding: ActivityComponentSelectionBinding
    private val viewModel: ArchitectViewModel by viewModels()

    /** Full ordered category flow per the spec */
    private val categories = ComponentCategory.values().toList() // MOTHERBOARD, CPU, GPU, PSU, SSD, RAM, CABINET, MONITOR

    private var currentCategoryIndex = 0
    private val currentCategory get() = categories[currentCategoryIndex]

    private lateinit var suggestionsAdapter: ComponentAdapter
    private lateinit var allAdapter: ComponentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComponentSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Receive brand + optional start category from MotherboardPickerActivity
        val brandFromIntent = intent.getStringExtra(EXTRA_SELECTED_BRAND)
        if (!brandFromIntent.isNullOrBlank()) {
            viewModel.selectedBrand = brandFromIntent
        }

        val startCatName = intent.getStringExtra(EXTRA_START_CATEGORY)
        if (!startCatName.isNullOrBlank()) {
            val idx = categories.indexOfFirst { it.name == startCatName }
            if (idx >= 0) currentCategoryIndex = idx
        }

        setupRecyclerViews()
        setupSearch()
        showCategory(currentCategoryIndex)

        binding.btnNext.setOnClickListener {
            if (!viewModel.selectedComponents.containsKey(currentCategory)) {
                Toast.makeText(
                    this, "Please select a ${currentCategory.displayName}", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            advance()
        }

        binding.btnSkip.setOnClickListener { advance() }
    }

    private fun setupRecyclerViews() {
        suggestionsAdapter = ComponentAdapter(emptyList(), null) { component ->
            viewModel.selectComponent(component)
            refreshSelectionUI(component.id)
            provideHapticFeedback()
            // Auto-advance after selection for a smoother "turn by turn" feel
            binding.root.postDelayed({ advance() }, 400)
        }
        allAdapter = ComponentAdapter(emptyList(), null) { component ->
            viewModel.selectComponent(component)
            refreshSelectionUI(component.id)
            provideHapticFeedback()
            // Auto-advance after selection
            binding.root.postDelayed({ advance() }, 400)
        }

        binding.rvSuggestions.apply {
            layoutManager = LinearLayoutManager(this@ComponentSelectionActivity)
            adapter = suggestionsAdapter
            isNestedScrollingEnabled = false
        }
        binding.rvComponents.apply {
            layoutManager = LinearLayoutManager(this@ComponentSelectionActivity)
            adapter = allAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                allAdapter.filter(query)
                // Hide suggestions section when searching
                val searching = query.isNotBlank()
                binding.tvSuggestionsHeader.visibility = if (searching) View.GONE else View.VISIBLE
                binding.rvSuggestions.visibility = if (searching) View.GONE else View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun showCategory(index: Int) {
        val category = categories[index]
        val brand = viewModel.selectedBrand.takeIf { it.isNotBlank() }

        // Only MOTHERBOARD and CPU are brand-gated
        val effectiveBrand = if (category == ComponentCategory.MOTHERBOARD ||
            category == ComponentCategory.CPU) brand else null

        val allComponents = ComponentDatabase.getComponentsByCategory(category, effectiveBrand)
        val topSuggestions = ComponentDatabase.getTopSuggestions(category, effectiveBrand, 5)

        val selectedId = viewModel.getSelected(category)?.id

        // Update UI labels
        binding.tvCategoryTitle.text = "${category.icon} ${category.displayName}"
        binding.tvAllComponentsHeader.text = "All ${category.displayName}s" // dynamic header
        binding.tvProgress.text = "Step ${index + 1} of ${categories.size}"
        binding.progressBar.progress = ((index + 1) * 100) / categories.size
        binding.btnNext.text = if (index == categories.size - 1) "Proceed to Budget ✓" else "Next Component →"

        // Clear search box on category change
        binding.etSearch.setText("")
        binding.tvSuggestionsHeader.visibility = View.VISIBLE
        binding.rvSuggestions.visibility = View.VISIBLE

        // Load both adapters
        suggestionsAdapter.replaceData(topSuggestions, selectedId)
        allAdapter.replaceData(allComponents, selectedId)

        // Selected indicator
        refreshSelectionUI(selectedId)
    }

    private fun refreshSelectionUI(selectedId: String?) {
        val sel = viewModel.getSelected(currentCategory)
        binding.tvSelectedComponent.text = if (sel != null)
            "✓ Selected: ${sel.name}" else "No component selected yet"

        // Update running total
        val total = viewModel.getTotalCost()
        binding.tvRunningTotal.text = "Current Build Total: ₹${String.format(Locale.getDefault(), "%,.0f", total)}"

        // Re-sync both adapters' selected state
        suggestionsAdapter.replaceData(
            ComponentDatabase.getTopSuggestions(
                currentCategory,
                if (currentCategory == ComponentCategory.MOTHERBOARD ||
                    currentCategory == ComponentCategory.CPU)
                    viewModel.selectedBrand.takeIf { it.isNotBlank() } else null,
                5
            ), selectedId
        )
        allAdapter.replaceData(
            ComponentDatabase.getComponentsByCategory(
                currentCategory,
                if (currentCategory == ComponentCategory.MOTHERBOARD ||
                    currentCategory == ComponentCategory.CPU)
                    viewModel.selectedBrand.takeIf { it.isNotBlank() } else null
            ), selectedId
        )
    }

    private fun advance() {
        if (currentCategoryIndex < categories.size - 1) {
            // Animate out current content
            binding.scrollContent.animate()
                .translationX(-binding.root.width.toFloat())
                .alpha(0f)
                .setDuration(250)
                .withEndAction {
                    currentCategoryIndex++
                    showCategory(currentCategoryIndex)
                    
                    // Reset position and animate in
                    binding.scrollContent.translationX = binding.root.width.toFloat()
                    binding.scrollContent.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(250)
                        .start()
                }
                .start()
        } else {
            val intent = Intent(this, BudgetInputActivity::class.java).apply {
                putParcelableArrayListExtra("selected_components", viewModel.getAllSelected())
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun provideHapticFeedback() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(30)
        }
    }
}
