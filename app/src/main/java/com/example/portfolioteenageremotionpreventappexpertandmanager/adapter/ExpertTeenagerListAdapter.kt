package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList.AllocatedTeenager

class ExpertTeenagerListAdapter(var expertTeenagerList: List<AllocatedTeenager>, private val onItemClick: (AllocatedTeenager) -> Unit) :
    RecyclerView.Adapter<ExpertTeenagerListAdapter.ExpertTeenagerViewHolder>() {

    class ExpertTeenagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertTeenagerInfoTextView: TextView = itemView.findViewById(R.id.expertTeenagerInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertTeenagerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expert_teenagerlist, parent, false)
        return ExpertTeenagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertTeenagerViewHolder, position: Int) {
        val allocatedTeenager = expertTeenagerList[position]
        val allocatedTeenagerInfo = "이름: ${allocatedTeenager.name}\n 아이디: ${allocatedTeenager.id}\n 비밀번호: ${allocatedTeenager.pw}\n" +
                "주소: ${allocatedTeenager.address}\n 핸드폰번호: ${allocatedTeenager.phone_num}\n 위험 상태: ${allocatedTeenager.at_risk_child_status}\n"+
                "감정: ${allocatedTeenager.sentiment}\n"

        holder.expertTeenagerInfoTextView.text = allocatedTeenagerInfo

        holder.itemView.setOnClickListener {
            onItemClick(allocatedTeenager)
        }
    }

    override fun getItemCount() = expertTeenagerList.size
}