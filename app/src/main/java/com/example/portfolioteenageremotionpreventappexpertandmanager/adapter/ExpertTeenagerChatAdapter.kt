package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerChat.ExpertTeenagerChatDataPair

class ExpertTeenagerChatAdapter(private val teenagerChatData: List<ExpertTeenagerChatDataPair>) :
    RecyclerView.Adapter<ExpertTeenagerChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputMessageTextView: TextView = itemView.findViewById(R.id.inputChatTextView)
        val responseMessageTextView: TextView = itemView.findViewById(R.id.responseChatTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_chat, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messagePair = teenagerChatData[position]

        if (messagePair.inputMessage.isNullOrEmpty()) {
            holder.inputMessageTextView.visibility = View.GONE
        } else {
            holder.inputMessageTextView.text = messagePair.inputMessage
            holder.inputMessageTextView.textSize = 15f
            holder.inputMessageTextView.visibility = View.VISIBLE
        }

        if (messagePair.responseMessage.isNullOrEmpty()) {
            holder.responseMessageTextView.visibility = View.GONE
        } else {
            holder.responseMessageTextView.text = messagePair.responseMessage
            holder.responseMessageTextView.textSize = 15f
            holder.responseMessageTextView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = teenagerChatData.size
}