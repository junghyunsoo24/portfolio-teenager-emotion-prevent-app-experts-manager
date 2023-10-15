package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList.Teenager

class ManagerTeenagerListAdapter(var managerTeenagerList: List<Teenager>, private val onItemClick: (Teenager) -> Unit) :
    RecyclerView.Adapter<ManagerTeenagerListAdapter.ManagerTeenagerViewHolder>() {

    class ManagerTeenagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val managerTeenagerInfoTextView: TextView = itemView.findViewById(R.id.managerTeenagerInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerTeenagerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_teenagerlist, parent, false)
        return ManagerTeenagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ManagerTeenagerViewHolder, position: Int) {
        val teenager = managerTeenagerList[position]
        val teenagerInfo = "아이디: ${teenager.id}\n 이름: ${teenager.name}\n" +
                "핸드폰번호: ${teenager.phone_num}\n"+
                "주소: ${teenager.address}\n"
        holder.managerTeenagerInfoTextView.text = teenagerInfo

        holder.itemView.setOnClickListener {
            onItemClick(teenager)
        }
    }

    override fun getItemCount() = managerTeenagerList.size
}