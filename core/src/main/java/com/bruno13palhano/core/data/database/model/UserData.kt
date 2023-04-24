package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.User

@Entity(tableName = "user_table")
internal data class UserData(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "baby_name")
    val babyName: String,

    @ColumnInfo(name = "baby_url_photo")
    val babyUrlPhoto: String,

    @ColumnInfo(name = "birth_place")
    val birthplace: String,

    @ColumnInfo(name = "birth_date")
    val birthdate: Long,

    @ColumnInfo(name = "birth_time")
    val birthtime: Long,

    @ColumnInfo(name = "height")
    val height: Float,

    @ColumnInfo(name = "weight")
    val weight: Float
)

internal fun UserData.asUser() = User(
    id = id,
    username = username,
    email = email,
    babyName = babyName,
    babyUrlPhoto = babyUrlPhoto,
    birthplace = birthplace,
    birthdate = birthdate,
    birthtime = birthtime,
    height = height,
    weight = weight
)

internal fun User.asUserData() = UserData(
    id = id,
    username = username,
    email = email,
    babyName = babyName,
    babyUrlPhoto = babyUrlPhoto,
    birthplace = birthplace,
    birthdate = birthdate,
    birthtime = birthtime,
    height = height,
    weight = weight
)
