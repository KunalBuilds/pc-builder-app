package com.mypcapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mypcapp.model.ComponentCategory
import com.mypcapp.model.PCComponent

/**
 * Shared ViewModel for the Architect Your PC flow.
 * Survives screen rotations and is shared between MotherboardPickerActivity
 * and ComponentSelectionActivity.
 */
class ArchitectViewModel : ViewModel() {

    /** "Intel" or "AMD" — chosen at the Motherboard Picker screen */
    var selectedBrand: String = ""

    /** Persisted selections keyed by category */
    val selectedComponents = mutableMapOf<ComponentCategory, PCComponent>()

    /** Live search query for the current step */
    val searchQuery = MutableLiveData("")

    fun selectComponent(component: PCComponent) {
        selectedComponents[component.category] = component
    }

    fun getSelected(category: ComponentCategory): PCComponent? =
        selectedComponents[category]

    fun getTotalCost(): Double {
        return selectedComponents.values.sumOf { it.price }
    }

    fun getAllSelected(): ArrayList<PCComponent> =
        ArrayList(selectedComponents.values.toList())

    fun clear() {
        selectedBrand = ""
        selectedComponents.clear()
        searchQuery.value = ""
    }
}
