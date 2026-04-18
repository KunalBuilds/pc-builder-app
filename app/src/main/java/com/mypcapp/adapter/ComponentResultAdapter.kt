package com.mypcapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypcapp.databinding.ItemComponentResultBinding
import com.mypcapp.model.PCComponent

class ComponentResultAdapter(
    private val components: List<PCComponent>,
    private val onBuyClick: (PCComponent, String) -> Unit
) : RecyclerView.Adapter<ComponentResultAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemComponentResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComponentResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val component = components[position]
        val b = holder.binding

        b.tvType.text = component.category.displayName.uppercase()
        b.tvName.text = component.name
        b.tvPrice.text = "₹${String.format("%,.0f", component.price)}"
        
        // Split specs for the result card design
        val specs = component.specsText.split("•")
        b.tvSpec1Label.text = "BRAND"
        b.tvSpec1Value.text = component.brand
        
        if (specs.isNotEmpty()) {
            b.tvSpec2Label.text = "SPEC"
            b.tvSpec2Value.text = specs[0].trim()
        }

        b.btnBuyNow.setOnClickListener { onBuyClick(component, "amazon") }
    }

    override fun getItemCount() = components.size
}
