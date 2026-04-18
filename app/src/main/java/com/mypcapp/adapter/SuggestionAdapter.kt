package com.mypcapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypcapp.databinding.ItemSuggestionBinding
import com.mypcapp.model.PCComponent

class SuggestionAdapter(
    private val suggestions: List<Pair<PCComponent, PCComponent>>
) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSuggestionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (current, better) = suggestions[position]
        val b = holder.binding

        b.tvCategory.text = current.category.displayName
        b.tvCurrentComponent.text = "Current: ${current.name}"
        b.tvCurrentPrice.text = "₹${String.format("%,.0f", current.price)}"
        b.tvBetterComponent.text = "Upgrade to: ${better.name}"
        b.tvBetterPrice.text = "₹${String.format("%,.0f", better.price)}"

        val scoreDiff = better.performanceScore - current.performanceScore
        val priceDiff = better.price - current.price
        b.tvUpgradeBenefit.text = "+$scoreDiff performance score • " +
            if (priceDiff > 0) "+₹${String.format("%,.0f", priceDiff)}" else "-₹${String.format("%,.0f", -priceDiff)}"
    }

    override fun getItemCount() = suggestions.size
}
