package com.mypcapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PCComponent(
    val id: String = "",
    val name: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val category: ComponentCategory = ComponentCategory.MOTHERBOARD,
    val specsText: String = "",
    val amazonLink: String = "",
    val flipkartLink: String = "",
    val imageUrl: String = "",
    val performanceScore: Int = 0,
    /** "Intel", "AMD", or "" for brand-agnostic components */
    val compatibleBrand: String = "",
    /** Drawable resource name (without extension) for product image, e.g. "gpu_rtx_3060" */
    val drawableResName: String = ""
) : Parcelable

@Parcelize
enum class ComponentCategory(val displayName: String, val icon: String) : Parcelable {
    MOTHERBOARD("Motherboard", "🔌"),
    CPU("Processor (CPU)", "🖥️"),
    GPU("Graphics Card (GPU)", "🎮"),
    PSU("Power Supply (PSU)", "⚡"),
    SSD("Storage (SSD)", "💿"),
    RAM("RAM Memory", "💾"),
    CABINET("Cabinet / Case", "🗄️"),
    MONITOR("Monitor", "🖥")
}
