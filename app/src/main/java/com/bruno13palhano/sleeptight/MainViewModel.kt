package com.bruno13palhano.sleeptight

import androidx.lifecycle.ViewModel
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @DefaultUserFirebase private val userAuthentication: UserAuthentication
) : ViewModel() {
    fun isUserAuthenticated() = userAuthentication.isUserAuthenticated()
}