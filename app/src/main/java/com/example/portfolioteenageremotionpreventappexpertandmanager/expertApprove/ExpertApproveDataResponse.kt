package com.example.portfolioteenageremotionpreventappexpertandmanager.expertApprove

data class ManagerExpertApproveDataResponse(
    val experts: List<Expert>
)

data class Expert(
    val key: Int,
    val id: String,
    val name: String,
    val email: String,
    val institution: String,
    val approval: String
)