package com.example.portfolioteenageremotionpreventappexpertandmanager.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioteenageremotionpreventappexpertandmanager.R
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerStatistics.Statistics
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ExpertTeenagerStatisticsAdapter(var teenagerStatistics: List<Statistics>, private val onItemClick: (Statistics) -> Unit) :
    RecyclerView.Adapter<ExpertTeenagerStatisticsAdapter.ExpertTeenagerStatisticsViewHolder>() {

    class ExpertTeenagerStatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertTeenagerStatisticsInfoTextView: TextView = itemView.findViewById(R.id.expertTeenagerInfoTextViews)
        val emotionChart: BarChart = itemView.findViewById(R.id.emotionChart)
        val emotionTwoChart: BarChart = itemView.findViewById(R.id.emotionTwoChart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertTeenagerStatisticsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expert_teenager_statistics, parent, false)
        return ExpertTeenagerStatisticsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertTeenagerStatisticsViewHolder, position: Int) {
        val statistics = teenagerStatistics[position]
        val statisticsInfo = String.format(
            "날짜: %s\n문장들: %s",
            statistics.date, statistics.sentences)

        holder.expertTeenagerStatisticsInfoTextView.text = statisticsInfo

        val chartData = getBarChartData(statistics)
        holder.emotionChart.data = BarData(chartData)

        val chartTwoData = getTwoBarChartData(statistics)
        holder.emotionTwoChart.data = BarData(chartTwoData)

        holder.itemView.setOnClickListener {
            onItemClick(statistics)
        }
    }

    private fun getBarChartData(statistics: Statistics): List<BarDataSet> {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, statistics.pleasure.toFloat()))
        entries.add(BarEntry(1f, statistics.anxiety.toFloat()))
        entries.add(BarEntry(2f, statistics.sorrow.toFloat()))
        entries.add(BarEntry(3f, statistics.embarrassed.toFloat()))
        entries.add(BarEntry(4f, statistics.anger.toFloat()))
        entries.add(BarEntry(5f, statistics.hurt.toFloat()))

        val dataSet = BarDataSet(entries, "1.기쁨, 2.불안, 3.슬픔, 4.당황, 5.화남, 6.상처").apply {
            setColors(Color.BLUE, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }

        return listOf(dataSet)
    }

    private fun getTwoBarChartData(statistics: Statistics): List<BarDataSet> {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, statistics.pleasure.toFloat()))
        entries.add(BarEntry(1f, (statistics.anxiety + statistics.sorrow + statistics.embarrassed + statistics.anger + statistics.hurt).toFloat()))


        val dataSet = BarDataSet(entries, "1.기쁨, 2.부정적감정들").apply {
            setColors(Color.BLUE, Color.RED)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }

        return listOf(dataSet)
    }

    override fun getItemCount() = teenagerStatistics.size
}
