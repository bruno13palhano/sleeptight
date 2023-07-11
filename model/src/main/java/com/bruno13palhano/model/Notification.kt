package com.bruno13palhano.model

/**
 * A class to model the user notifications.
 *
 * @property id the id to identify this Notification.
 * @property title the title of this Notification.
 * @property description the description of this Notification.
 * @property time the time in milliseconds when this Notification will start.
 * @property date the date in milliseconds when this Notification will start.
 * @property repeat defines if the Notification will be repeated every day.
 */
data class Notification(
    val id: Long,
    val title: String,
    val description: String,
    val time: Long,
    val date: Long,
    val repeat: Boolean
)
