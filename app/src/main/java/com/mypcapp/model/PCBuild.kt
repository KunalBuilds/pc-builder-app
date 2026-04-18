package com.mypcapp.model

data class PCBuild(
    val id: String = "",
    val userId: String = "",
    val name: String = "My PC Build",
    val budget: Double = 0.0,
    val totalCost: Double = 0.0,
    val components: Map<String, PCComponent> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
)
