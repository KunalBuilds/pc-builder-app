package com.mypcapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypcapp.databinding.ItemChatBotBinding
import com.mypcapp.databinding.ItemChatUserBinding
import com.mypcapp.model.ChatMessage

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    inner class UserViewHolder(val binding: ItemChatUserBinding) : RecyclerView.ViewHolder(binding.root)
    inner class BotViewHolder(val binding: ItemChatBotBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            UserViewHolder(ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            BotViewHolder(ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.binding.tvUserMessage.text = message.content
            is BotViewHolder -> holder.binding.tvBotMessage.text = message.content
        }
    }

    override fun getItemCount() = messages.size
}
