package com.mypcapp.data

import com.mypcapp.model.ComponentCategory
import com.mypcapp.model.PCComponent

/**
 * Smart budget allocation engine.
 *
 * Strategy:
 *  1. Allocate each category a % target of the total budget
 *  2. Pick the best component whose price <= that category's allocation
 *  3. If nothing fits, pick the cheapest available
 *  4. Remaining unspent budget is redistributed proportionally to GPU and CPU
 */
object AiBudgetBuilder {

    /**
     * Budget allocation ratios (must sum to 1.0).
     * GPU and CPU get the biggest share; Monitor/Cabinet are secondary.
     */
    private val allocations = mapOf(
        ComponentCategory.MOTHERBOARD to 0.12,
        ComponentCategory.CPU         to 0.22,
        ComponentCategory.GPU         to 0.30,
        ComponentCategory.PSU         to 0.08,
        ComponentCategory.SSD         to 0.07,
        ComponentCategory.RAM         to 0.08,
        ComponentCategory.CABINET     to 0.06,
        ComponentCategory.MONITOR     to 0.07
    )

    fun buildPc(budget: Double, preselected: List<PCComponent> = emptyList()): List<PCComponent> {
        val result = mutableListOf<PCComponent>()
        val preselectedCategories = preselected.map { it.category }.toSet()
        
        // Use preselected components first
        result.addAll(preselected)
        
        var remainingBudget = budget - preselected.sumOf { it.price }
        
        // Filter out categories already selected by user
        val categoriesToPick = ComponentCategory.values().filter { it !in preselectedCategories }
        
        if (categoriesToPick.isEmpty()) return result.sortedBy { it.category.ordinal }

        // Recalculate allocation weights for remaining categories
        val totalRemainingWeight = categoriesToPick.sumOf { allocations[it] ?: 0.05 }
        
        // Pick for remaining categories
        for (i in categoriesToPick.indices) {
            val category = categoriesToPick[i]
            
            // For the last category, use all remaining budget
            val targetSpend = if (i == categoriesToPick.size - 1) {
                remainingBudget
            } else {
                val weight = (allocations[category] ?: 0.05) / totalRemainingWeight
                budget * weight
            }

            val pick = pickBest(category, minOf(targetSpend, remainingBudget))
            if (pick != null) {
                result.add(pick)
                remainingBudget -= pick.price
            }
        }

        return result.sortedBy { it.category.ordinal }
    }

    /**
     * Picks the highest-performance component within [maxPrice].
     * Falls back to cheapest option if nothing fits the budget cap.
     */
    private fun pickBest(category: ComponentCategory, maxPrice: Double): PCComponent? {
        val all = ComponentDatabase.getComponentsByCategory(category)
        val affordable = all.filter { it.price <= maxPrice }
        return if (affordable.isNotEmpty()) {
            affordable.maxByOrNull { it.performanceScore }
        } else {
            all.minByOrNull { it.price } // cheapest fallback
        }
    }
}
