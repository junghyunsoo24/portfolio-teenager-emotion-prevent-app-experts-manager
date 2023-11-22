package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertApprove.Expert

class ManagerExpertApproveAdapter(var expertList: List<Expert>, private val onItemClick: (Expert) -> Unit) :
    RecyclerView.Adapter<ManagerExpertApproveAdapter.ExpertViewHolder>() {

    class ExpertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertInfoTextView: TextView = itemView.findViewById(R.id.expertInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_expertlist, parent, false)
        return ExpertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertViewHolder, position: Int) {
        val expert = expertList[position]
        val expertInfo = String.format(
            "아이디: %s\n이름: %s\n이메일: %s\n기관: %s\n",
            expert.id,
            expert.name,
            expert.email,
            expert.institution
        )
        holder.expertInfoTextView.text = expertInfo

        holder.itemView.setOnClickListener {
            onItemClick(expert)
        }
    }

    override fun getItemCount() = expertList.size
}