package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.User

/**
 * User as an Entity.
 *
 * An entity class to persist the User in database.
 * @property id the id to identify this User account.
 * @property username the username.
 * @property email the User email.
 * @property babyName the name of the baby related to this User account.
 * @property babyUrlPhoto the Url of the User's profile picture.
 * @property birthplace baby's place of birth.
 * @property birthdate baby's date of birth in milliseconds.
 * @property birthtime baby's time of birth in milliseconds.
 * @property height baby's height at birth.
 * @property weight baby's weight at birth.
 */
@Entity(tableName = "user_table")
internal data class UserEntity(

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

/**
 * Transforms [UserEntity] into [User].
 * @return [User].
 */
internal fun UserEntity.asUser() = User(
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

/**
 * Transforms [User] into [UserEntity].
 * @return [UserEntity]
 */
internal fun User.asUserEntity() = UserEntity(
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
