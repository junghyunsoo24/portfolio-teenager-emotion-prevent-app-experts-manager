package com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertList

data class ManagerExpertListDataResponse(
    val expert: List<Expert>
)

data class Expert(
    val key: Int,
    val id: String,
    val name: String,
    val email: String,
    val institution: String,
    val approval: String
)