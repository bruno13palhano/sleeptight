package com.bruno13palhano.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Notification

/**
 * Notification as an Entity.
 *
 * An entity class to persist the Notifications in database.
 * @property id the id to identify this Notification.
 * @property title the of this Notification.
 * @property description the description of this Notification entity.
 * @property time the time in milliseconds when this Notification will start.
 * @property date the date in milliseconds when this Notification will start.
 * @property repeat defines if the Notification will be repeated every day.
 */
@Entity(tableName = "notification_table")
internal data class NotificationEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "repeat")
    val repeat: Boolean,
)

/**
 * Transforms [NotificationEntity] into [Notification].
 * @return [Notification].
 */
internal fun NotificationEntity.asNotification() = Notification(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    repeat = repeat,
)

/**
 * Transforms [Notification] into [NotificationEntity].
 * @return [NotificationEntity].
 */
internal fun Notification.asNotificationEntity() = NotificationEntity(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    repeat = repeat,
)
