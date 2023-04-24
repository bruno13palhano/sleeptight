package com.bruno13palhano.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val babyName: String = "",
    val babyUrlPhoto: String = "",
    val birthplace: String = "",
    val birthdate: Long = 0L,
    val birthtime: Long = 0L,
    val height: Float = 0F,
    val weight: Float = 0F
)
