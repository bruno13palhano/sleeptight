package com.bruno13palhano.model

/**
 * A class to model the baby's status.
 *
 * @property id the id to identify this BabyStatus.
 * @property title the title of this BabyStatus.
 * @property date the date in milliseconds when this BabyStatus was measured.
 * @property height the height when this BabyStatus was measured.
 * @property weight the weight when this BabyStatus was measured.
 */
data class BabyStatus(
    val id: Long,
    val title: String,
    val date: Long,
    val height: Float,
    val weight: Float,
)
