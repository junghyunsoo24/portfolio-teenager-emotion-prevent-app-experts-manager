package com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerStatistics

import java.util.*

data class ExpertTeenagerStatisticsDataResponse(
    val statisticsArray: List<Statistics>
)

data class Statistics(
    val date: Date,
    val pleasure: Int,
    val anxiety: Int,
    val sorrow: Int,
    val embarrassed: Int,
    val anger: Int,
    val hurt: Int
)