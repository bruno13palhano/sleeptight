package com.bruno13palhano.sleeptight.ui.home

import androidx.lifecycle.ViewModel
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication
) : ViewModel() {

    fun isUserAuthenticated(): Boolean =
        authentication.isUserAuthenticated()
}