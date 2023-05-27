package com.bruno13palhano.model

data class Nap (
    val id: Long,
    val title: String,
    val date: Long,
    val startTime: Long,
    val endTime: Long,
    val sleepingTime: Long,
    val observation: String
)