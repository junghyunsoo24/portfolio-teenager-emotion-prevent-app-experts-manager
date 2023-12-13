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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class ExpertTeenagerStatisticsAdapter(var teenagerStatistics: List<Statistics>, private val onItemClick: (Statistics) -> Unit) : RecyclerView.Adapter<ExpertTeenagerStatisticsAdapter.ExpertTeenagerStatisticsViewHolder>() {

    class ExpertTeenagerStatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertTeenagerStatisticsInfoTextView: TextView = itemView.findViewById(R.id.expertTeenagerInfoTextViews)
        val emotionChart: BarChart = itemView.findViewById(R.id.emotionChart)
        val emotionTwoChart: PieChart = itemView.findViewById(R.id.emotionTwoChart)
//        val emotionLineChart: LineChart = itemView.findViewById(R.id.emotionLineChart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertTeenagerStatisticsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expert_teenager_statistics, parent, false)
        return ExpertTeenagerStatisticsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertTeenagerStatisticsViewHolder, position: Int) {
        val statistics = teenagerStatistics[position]
        val statisticsInfo = String.format(
            "<날짜: %s>\n\n문장들: %s",
            statistics.date, statistics.sentences)

        holder.expertTeenagerStatisticsInfoTextView.text = statisticsInfo

        val chartData = getBarChartData(statistics)
        holder.emotionChart.data = chartData

        val chartTwoData = getTwoPieChartData(statistics)
        holder.emotionTwoChart.data = PieData(chartTwoData)

//        val lineData = getLineChartData(holder, statistics)
//        holder.emotionLineChart.data = lineData
//        holder.emotionLineChart.description = null

        holder.emotionChart.description = null
        holder.emotionTwoChart.description = null


        holder.itemView.setOnClickListener {
            onItemClick(statistics)
        }
    }

    private val pastelPink = Color.rgb(255, 182, 193)

    private val indianRed = Color.rgb(205, 92, 92)
    private val salmon = Color.rgb(250, 128, 114)
    private val coral = Color.rgb(255, 127, 80)
    private val lightSalmon = Color.rgb(255, 160, 122)
    private val mistyRose = Color.rgb(255, 228, 225)

    private val pastelBlue = Color.rgb(173, 216, 230)
    private val skyBlue = Color.rgb(135, 206, 235)

    private fun getBarChartData(statistics: Statistics): BarData {
        val pleasureEntry = BarEntry(0f, statistics.pleasure.toFloat())
        val anxietyEntry = BarEntry(1f, statistics.anxiety.toFloat())
        val sorrowEntry = BarEntry(2f, statistics.sorrow.toFloat())
        val embarrassedEntry = BarEntry(3f, statistics.embarrassed.toFloat())
        val angerEntry = BarEntry(4f, statistics.anger.toFloat())
        val hurtEntry = BarEntry(5f, statistics.hurt.toFloat())

        val pleasureDataSet = BarDataSet(listOf(pleasureEntry), "기쁨").apply { setColors(skyBlue)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val anxietyDataSet = BarDataSet(listOf(anxietyEntry), "불안").apply { setColors(indianRed)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val sorrowDataSet = BarDataSet(listOf(sorrowEntry), "슬픔").apply { setColors(salmon)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val embarrassedDataSet = BarDataSet(listOf(embarrassedEntry), "당황").apply { setColors(coral)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val angerDataSet = BarDataSet(listOf(angerEntry), "화남").apply { setColors(lightSalmon)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val hurtDataSet = BarDataSet(listOf(hurtEntry), "상처").apply { setColors(mistyRose)
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }

        val decimalFormat = DecimalFormat("#")
        val valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return decimalFormat.format(value.toDouble())
            }
        }
        pleasureDataSet.valueFormatter = valueFormatter
        anxietyDataSet.valueFormatter = valueFormatter
        sorrowDataSet.valueFormatter = valueFormatter
        embarrassedDataSet.valueFormatter = valueFormatter
        angerDataSet.valueFormatter = valueFormatter
        hurtDataSet.valueFormatter = valueFormatter

        return BarData(pleasureDataSet, anxietyDataSet, sorrowDataSet, embarrassedDataSet, angerDataSet, hurtDataSet)
    }

    private fun getTwoPieChartData(statistics: Statistics): PieDataSet {
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(statistics.pleasure.toFloat(), "긍정"))
        entries.add(PieEntry((statistics.anxiety + statistics.sorrow + statistics.embarrassed + statistics.anger + statistics.hurt).toFloat(), "부정"))

        val dataSet = PieDataSet(entries, "").apply { setColors(pastelBlue, pastelPink)
            valueTextColor = Color.WHITE
            valueTextSize = 16f
        }

        val decimalFormat = DecimalFormat("#")
        val valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return decimalFormat.format(value.toDouble())
            }
        }

        dataSet.valueFormatter = valueFormatter

        return dataSet
    }

    override fun getItemCount() = teenagerStatistics.size
}
