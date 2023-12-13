package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertList.ApproveExpert


class ExpertListAdapter(var approveExpertList: List<ApproveExpert>, private val onItemClick: (ApproveExpert) -> Unit) : RecyclerView.Adapter<ExpertListAdapter.ExpertViewHolder>() {

    class ExpertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertInfoTextView: TextView = itemView.findViewById(R.id.expertInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_expertlist, parent, false)
        return ExpertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertViewHolder, position: Int) {
        val approveExpert = approveExpertList[position]

        val approveExpertInfo = String.format("아이디: %s\n이름: %s\n이메일: %s\n기관: %s\n",
            approveExpert.id, approveExpert.name, approveExpert.email, approveExpert.institution)

        holder.expertInfoTextView.text = approveExpertInfo
        holder.itemView.setOnClickListener {
            onItemClick(approveExpert)
        }
    }

    override fun getItemCount() = approveExpertList.size
}