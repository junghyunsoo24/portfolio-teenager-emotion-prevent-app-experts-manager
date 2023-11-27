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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ExpertTeenagerStatisticsAdapter(var teenagerStatistics: List<Statistics>, private val onItemClick: (Statistics) -> Unit) :
    RecyclerView.Adapter<ExpertTeenagerStatisticsAdapter.ExpertTeenagerStatisticsViewHolder>() {

    class ExpertTeenagerStatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertTeenagerStatisticsInfoTextView: TextView = itemView.findViewById(R.id.expertTeenagerInfoTextViews)
        val emotionChart: BarChart = itemView.findViewById(R.id.emotionChart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertTeenagerStatisticsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expert_teenager_statistics, parent, false)
        return ExpertTeenagerStatisticsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertTeenagerStatisticsViewHolder, position: Int) {
        val statistics = teenagerStatistics[position]
        val statisticsInfo = " 날짜: ${statistics.date}\n 기쁨: ${statistics.pleasure}\n 불안: ${statistics.anxiety}\n" +
                "슬픔: ${statistics.sorrow}\n 당황: ${statistics.embarrassed}\n 화남: ${statistics.anger}\n"+
                "상처: ${statistics.hurt}\n \"문장들: ${statistics.sentences}\\n"

        holder.expertTeenagerStatisticsInfoTextView.text = statisticsInfo

        // 차트 데이터 설정
        val chartData = getBarChartData(statistics)
        holder.emotionChart.data = BarData(chartData)

        holder.itemView.setOnClickListener {
            onItemClick(statistics)
        }
    }

    private fun getChartData(statistics: Statistics): List<ILineDataSet> {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, statistics.pleasure.toFloat()))
        entries.add(Entry(1f, statistics.anxiety.toFloat()))
        entries.add(Entry(2f, statistics.sorrow.toFloat()))
        entries.add(Entry(3f, statistics.embarrassed.toFloat()))
        entries.add(Entry(4f, statistics.anger.toFloat()))
        entries.add(Entry(5f, statistics.hurt.toFloat()))

        val dataSet = LineDataSet(entries, "감정분석 결과")

        val colors = mutableListOf<Int>()
        colors.add(Color.BLUE)    // 기쁨
        colors.add(Color.RED)     // 불안
        colors.add(Color.RED)     // 슬픔
        colors.add(Color.RED)     // 당황
        colors.add(Color.RED)     // 화남
        colors.add(Color.RED)     // 상처

        dataSet.colors = colors
        dataSet.valueTextColor = Color.BLACK

        return listOf(dataSet)
    }

    private fun getBarChartData(statistics: Statistics): List<BarDataSet> {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, statistics.pleasure.toFloat()))
        entries.add(BarEntry(1f, statistics.anxiety.toFloat()))
        entries.add(BarEntry(2f, statistics.sorrow.toFloat()))
        entries.add(BarEntry(3f, statistics.embarrassed.toFloat()))
        entries.add(BarEntry(4f, statistics.anger.toFloat()))
        entries.add(BarEntry(5f, statistics.hurt.toFloat()))

        val dataSet = BarDataSet(entries, "Emotion Values")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        return listOf(dataSet)
    }

    override fun getItemCount() = teenagerStatistics.size
}