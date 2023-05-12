package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Notification

@Entity(tableName = "notification_table")
internal data class NotificationData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "repeat")
    val repeat: Boolean
)

internal fun NotificationData.asNotification() = Notification(
    id = id,
    title = title,
    description = description,
    time = time,
    repeat = repeat
)

internal fun Notification.asNotificationData() = NotificationData(
    id = id,
    title = title,
    description = description,
    time = time,
    repeat = repeat
)
