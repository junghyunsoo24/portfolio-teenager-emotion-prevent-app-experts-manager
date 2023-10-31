package com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList

data class ExpertTeenagerListDataResponse(
    val result: List<AllocatedTeenager>
)

data class AllocatedTeenager(
    val id: String,
    val pw: String,
    val name: String,
    val age: Int,
    val address: String,
    val phone_num: String,
    val at_risk_child_status: Int,
    val sentiment: Int
)