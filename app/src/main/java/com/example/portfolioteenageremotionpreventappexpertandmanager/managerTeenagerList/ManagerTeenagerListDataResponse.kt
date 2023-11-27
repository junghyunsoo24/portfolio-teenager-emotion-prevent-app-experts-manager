package com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList

data class ManagerTeenagerListDataResponse (
    val teenagers: List<Teenager>
)

data class Teenager(
    val key: Int,
    val id: String,
    val name: String,
    val age: Int,
    val address: String,
    val gender: String,
    val phone_num: String,
    val assignments: Int,
    val percentage: Int

)