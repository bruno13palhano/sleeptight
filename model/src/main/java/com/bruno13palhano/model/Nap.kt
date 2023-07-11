package com.bruno13palhano.model

/**
 * A class to model the baby's naps.
 *
 * @property id the id to identify this Nap.
 * @property title the title of this Nap.
 * @property date the date in milliseconds that this Nap occurred.
 * @property startTime the time this Nap started in milliseconds.
 * @property endTime the time this Nap ended in milliseconds.
 * @property sleepingTime the Nap duration in milliseconds.
 * @property observation the observations about this Nap.
 */
data class Nap (
    val id: Long,
    val title: String,
    val date: Long,
    val startTime: Long,
    val endTime: Long,
    val sleepingTime: Long,
    val observation: String
)