package com.bruno13palhano.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val babyName: String = "",
    val birthLocal: String = "",
    val birthdate: Long = 0L,
    val birthTime: Long = 0L,
    val height: Float = 0F,
    val weight: Float = 0F
)
