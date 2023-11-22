package com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList

data class ExpertTeenagerListDataResponse(
    val teenagers: List<AllocatedTeenager>
)

data class AllocatedTeenager(
    val key: Int,
    val id: String,
    val name: String,
    val age: Int,
    val address: String,
    val gender: String,
    val phone_num: String,
    val assignments: Int
)