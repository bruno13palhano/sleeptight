package com.bruno13palhano.sleeptight.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository,
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {
    private val _babyName = MutableStateFlow("")
    val babyName = _babyName.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _height = MutableStateFlow(0F)
    val height = _height.asStateFlow()
        .map { "$it cm" }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _weight = MutableStateFlow(0F)
    val weight = _weight.asStateFlow()
        .map { "$it kg" }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    init {
        viewModelScope.launch {
            userRepository.getUserByIdStream(authentication.getCurrentUser().id).collect {
                _babyName.value = it.babyName
                _profileImage.value = it.babyUrlPhoto
            }
        }

        viewModelScope.launch {
            babyStatusRepository.getLastBabyStatusStream().collect {
                _height.value = it.height
                _weight.value = it.weight
            }
        }
    }

    fun isUserAuthenticated(): Boolean =
        authentication.isUserAuthenticated()
}