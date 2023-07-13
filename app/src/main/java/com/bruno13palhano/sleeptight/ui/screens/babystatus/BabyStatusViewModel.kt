package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyStatusViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>
) : ViewModel() {
    var title by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date by mutableStateOf("")
        private set
    var height by mutableStateOf("")
        private set
    var weight by mutableStateOf("")
        private set

    val isTitleNotEmpty = title.trim() != ""

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = DateFormatUtil.format(dateInMillis)
    }

    fun updateHeight(height: String) {
        this.height = height
    }

    fun updateWeight(weight: String) {
        this.weight = weight
    }

    fun getBabyStatus(id: Long) {
        viewModelScope.launch {
            babyStatusRepository.getById(id).collect {
                title = it.title
                height = it.height.toString()
                weight = it.weight.toString()
                updateDate(it.date)
            }
        }
    }

    fun deleteBabyStatus(id: Long) {
        viewModelScope.launch {
            babyStatusRepository.deleteById(id)
        }
    }

    fun updateBabyStatus(id: Long) {
        viewModelScope.launch {
            val babyStatus = BabyStatus(
                id = id,
                title = title,
                date = dateInMillis,
                height = stringToFloat(height),
                weight = stringToFloat(weight)
            )
            babyStatusRepository.update(babyStatus)
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) {
            0F
        }
    }
}