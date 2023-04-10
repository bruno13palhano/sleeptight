package com.bruno13palhano.model

data class Nap (
    val id: Long,
    val date: Long,
    val startTime: Long,
    val endTime: Long,
    val observation: String
)