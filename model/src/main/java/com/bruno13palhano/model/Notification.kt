package com.bruno13palhano.model

data class Notification(
    val id: Long,
    val title: String,
    val description: String,
    val time: Long,
    val repeat: Boolean
)
