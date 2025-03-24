package com.bruno13palhano.model

/**
 * A class to model a User account.
 *
 * @property id the id to identify this User account.
 * @property username the username.
 * @property email the User email.
 * @property password the User password.
 * @property babyName the name of the baby related to this User account.
 * @property babyUrlPhoto the Url of the User's profile picture.
 * @property birthplace baby's place of birth.
 * @property birthdate baby's date of birth in milliseconds.
 * @property birthtime baby's time of birth in milliseconds.
 * @property height baby's height at birth.
 * @property weight baby's weight at birth.
 */
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val babyName: String = "",
    val babyUrlPhoto: String = "",
    val birthplace: String = "",
    val birthdate: Long = 0L,
    val birthtime: Long = 0L,
    val height: Float = 0F,
    val weight: Float = 0F,
)
