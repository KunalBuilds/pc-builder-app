package com.mypcapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypcapp.databinding.ItemRigVaultBinding
import com.mypcapp.model.PCBuild

class RigVaultAdapter(
    private val builds: List<PCBuild>,
    private val onItemClick: (PCBuild) -> Unit
) : RecyclerView.Adapter<RigVaultAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRigVaultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRigVaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val build = builds[position]
        holder.binding.tvBuildName.text = build.name
        holder.binding.tvBuildCost.text = "₹${String.format("%,.0f", build.totalCost)}"
        holder.binding.tvComponentCount.text = "${build.components.size} Parts"
        
        holder.binding.root.setOnClickListener { onItemClick(build) }
    }

    override fun getItemCount() = builds.size
}
