package com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList

data class ManagerTeenagerListDataResponse (
    val child: List<Teenager>
)

data class Teenager(
    val id: String,
    val name: String,
    val phone_num: String,
    val address: String
)