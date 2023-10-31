package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerStatistics.Statistics

class ExpertTeenagerStatisticsAdapter(var teenagerStatistics: List<Statistics>, private val onItemClick: (Statistics) -> Unit) :
    RecyclerView.Adapter<ExpertTeenagerStatisticsAdapter.ExpertTeenagerStatisticsViewHolder>() {

    class ExpertTeenagerStatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertTeenagerStatisticsInfoTextView: TextView = itemView.findViewById(R.id.expertTeenagerInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertTeenagerStatisticsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expert_teenagerlist, parent, false)
        return ExpertTeenagerStatisticsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertTeenagerStatisticsViewHolder, position: Int) {
        val statistics = teenagerStatistics[position]
        val statisticsInfo = " 날짜: ${statistics.date}\n 기쁨: ${statistics.pleasure}\n 불안: ${statistics.anxiety}\n" +
                "슬픔: ${statistics.sorrow}\n 당황: ${statistics.embarrassed}\n 화남: ${statistics.anger}\n"+
                "상처: ${statistics.hurt}\n"

        holder.expertTeenagerStatisticsInfoTextView.text = statisticsInfo

        holder.itemView.setOnClickListener {
            onItemClick(statistics)
        }
    }

    override fun getItemCount() = teenagerStatistics.size
}