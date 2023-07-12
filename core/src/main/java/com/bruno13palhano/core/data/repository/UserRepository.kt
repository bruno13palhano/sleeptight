package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.database.data.UserDataContract
import com.bruno13palhano.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository: UserDataContract<User>