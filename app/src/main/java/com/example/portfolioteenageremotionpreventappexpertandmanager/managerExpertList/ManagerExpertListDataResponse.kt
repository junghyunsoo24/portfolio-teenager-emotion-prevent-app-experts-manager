package com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertList

data class ManagerExpertListDataResponse(
    val authorizedExperts: List<ApproveExpert>
)

data class ApproveExpert(
    val key: Int,
    val id: String,
    val name: String,
    val email: String,
    val institution: String,
    val approval: String?
)