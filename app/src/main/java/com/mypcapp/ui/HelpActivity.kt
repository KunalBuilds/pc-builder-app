package com.mypcapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypcapp.adapter.ChatAdapter
import com.mypcapp.databinding.ActivityHelpBinding
import com.mypcapp.model.ChatMessage
import com.mypcapp.network.GeminiRepository
import kotlinx.coroutines.launch

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    /** Full conversation history for multi-turn Gemini context. */
    private val chatHistory = mutableListOf<Pair<Boolean, String>>()
    private var isSending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupInputHandlers()
        showWelcomeMessage()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.rvMessages.adapter = chatAdapter
    }

    private fun showWelcomeMessage() {
        addBotMessage(
            "👋 Hi! I'm your AI PC Building Expert, powered by Google Gemini.\n\n" +
            "Ask me anything:\n" +
            "• \"Best PC under ₹60,000?\"\n" +
            "• \"RTX 3060 vs RX 6600 XT?\"\n" +
            "• \"Is my build compatible?\"\n" +
            "• \"Best GPU for 1440p gaming?\"\n\n" +
            "I'll give you real, specific answers! 🚀"
        )
    }

    private fun setupInputHandlers() {
        // Send button (now a LinearLayout in new design)
        binding.btnSend.setOnClickListener {
            submitMessage()
        }

        // Enter key
        binding.etMessage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                submitMessage()
                true
            } else {
                false
            }
        }

        // Suggestion chips
        binding.chipCompareCPU.setOnClickListener {
            sendMessage("Compare the best CPUs for gaming under ₹30,000 in India.")
        }
        binding.chipRecommendGPU.setOnClickListener {
            sendMessage("Recommend the best GPU for 1440p gaming under ₹40,000.")
        }
        binding.chipPSUCheck.setOnClickListener {
            sendMessage("How do I calculate the right power supply wattage for my PC build?")
        }
        binding.chipBottleneck.setOnClickListener {
            sendMessage("How do I check if my CPU and GPU will bottleneck each other?")
        }
    }

    private fun submitMessage() {
        if (isSending) return
        val text = binding.etMessage.text?.toString()?.trim() ?: ""
        if (text.isNotEmpty()) {
            binding.etMessage.text?.clear()
            sendMessage(text)
        }
    }

    private fun sendMessage(userText: String) {
        if (isSending) return
        isSending = true

        addUserMessage(userText)
        setInputEnabled(false)

        lifecycleScope.launch {
            try {
                val reply = GeminiRepository.chat(chatHistory, userText)
                chatHistory.add(Pair(true, userText))
                chatHistory.add(Pair(false, reply))
                addBotMessage(reply)
            } catch (e: Exception) {
                addBotMessage("⚠️ Something went wrong. Please check your connection and try again.")
            } finally {
                setInputEnabled(true)
                isSending = false
            }
        }
    }

    private fun setInputEnabled(enabled: Boolean) {
        binding.btnSend.isEnabled = enabled
        binding.etMessage.isEnabled = enabled
        binding.btnSend.alpha = if (enabled) 1f else 0.5f
    }

    private fun addUserMessage(text: String) {
        messages.add(ChatMessage(text, isUser = true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    private fun addBotMessage(text: String) {
        messages.add(ChatMessage(text, isUser = false))
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            binding.rvMessages.smoothScrollToPosition(messages.size - 1)
        }
    }
}
