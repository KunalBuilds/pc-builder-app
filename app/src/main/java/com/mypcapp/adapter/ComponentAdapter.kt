package com.mypcapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypcapp.databinding.ItemComponentBinding
import com.mypcapp.model.PCComponent

class ComponentAdapter(
    private var components: List<PCComponent>,
    private var selectedId: String? = null,
    private val onSelect: (PCComponent) -> Unit
) : RecyclerView.Adapter<ComponentAdapter.ViewHolder>() {

    /** Full list kept for search filtering */
    private var fullList: List<PCComponent> = components.toList()

    inner class ViewHolder(val binding: ItemComponentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val component = components[position]
        val b = holder.binding
        val isSelected = component.id == selectedId

        b.tvName.text = component.name
        b.tvBrand.text = component.brand
        b.tvPrice.text = "₹${String.format("%,.0f", component.price)}"
        
        // Use tvRating for performance score if that's what's available in XML
        b.tvRating.text = "${component.performanceScore}/100"

        // GPU product image
        if (component.drawableResName.isNotBlank()) {
            val resId = b.root.context.resources.getIdentifier(
                component.drawableResName, "drawable", b.root.context.packageName
            )
            if (resId != 0) {
                b.ivComponent.setImageResource(resId)
            } else {
                b.ivComponent.setImageResource(com.mypcapp.R.drawable.pc_builder_icon)
            }
        } else {
            b.ivComponent.setImageResource(com.mypcapp.R.drawable.pc_builder_icon)
        }

        // Selection state
        val context = b.root.context
        if (isSelected) {
            b.root.setBackgroundResource(com.mypcapp.R.drawable.bg_component_selected)
            b.btnAdd.setBackgroundResource(com.mypcapp.R.drawable.btn_neon_gradient)
            // If btnAdd contains a TextView (it's a LinearLayout in XML), find it
            (b.btnAdd.getChildAt(0) as? android.widget.TextView)?.setTextColor(0xFFFFFFFF.toInt())
        } else {
            b.root.setBackgroundResource(com.mypcapp.R.color.surface_container_low)
            b.btnAdd.setBackgroundColor(context.getColor(com.mypcapp.R.color.surface_container_lowest))
            (b.btnAdd.getChildAt(0) as? android.widget.TextView)?.setTextColor(context.getColor(com.mypcapp.R.color.on_surface_variant))
        }

        b.root.setOnClickListener {
            val old = selectedId
            selectedId = component.id
            // Refresh only changed items for performance
            val oldIdx = components.indexOfFirst { it.id == old }
            val newIdx = holder.adapterPosition
            if (oldIdx >= 0) notifyItemChanged(oldIdx)
            if (newIdx != oldIdx) notifyItemChanged(newIdx)
            onSelect(component)
        }

        b.btnAdd.setOnClickListener {
            onSelect(component)
        }
    }

    override fun getItemCount() = components.size

    /** Call this to push a live-search filter */
    fun filter(query: String) {
        components = if (query.isBlank()) {
            fullList
        } else {
            val q = query.trim().lowercase()
            fullList.filter {
                it.name.lowercase().contains(q) ||
                it.brand.lowercase().contains(q) ||
                it.specsText.lowercase().contains(q)
            }
        }
        notifyDataSetChanged()
    }

    /** Replace entire dataset (e.g. when switching categories) */
    fun replaceData(newList: List<PCComponent>, newSelectedId: String?) {
        fullList = newList.toList()
        components = newList.toList()
        selectedId = newSelectedId
        notifyDataSetChanged()
    }
}
