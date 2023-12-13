package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList.AllocatedTeenager

class ExpertTeenagerListAdapter(var expertTeenagerList: List<AllocatedTeenager>, private val onItemClick: (AllocatedTeenager) -> Unit) : RecyclerView.Adapter<ExpertTeenagerListAdapter.ExpertTeenagerViewHolder>() {

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
        var gender = "0"
        if(allocatedTeenager.gender == "0") { gender = "남" }
        else if(allocatedTeenager.gender == "1"){ gender = "여" }

        val allocatedTeenagerInfo = String.format("아이디: %s\n이름: %s\n나이: %s\n주소: %s\n성별: %s\n핸드폰번호: %s\n위험 비율: %s\n",
            allocatedTeenager.id, allocatedTeenager.name, allocatedTeenager.age, allocatedTeenager.address, gender, allocatedTeenager.phone_num, allocatedTeenager.percentage)

        holder.expertTeenagerInfoTextView.text = allocatedTeenagerInfo

        holder.itemView.setOnClickListener {
            onItemClick(allocatedTeenager)
        }
    }

    override fun getItemCount() = expertTeenagerList.size
}