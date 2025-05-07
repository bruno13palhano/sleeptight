package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.di.BabyStatusRep
import com.bruno13palhano.core.repository.BabyStatusRepository
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.measureWithLocalDecimal
import com.bruno13palhano.sleeptight.ui.util.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class BabyStatusViewModel @Inject constructor(
    @BabyStatusRep private val babyStatusRepository: BabyStatusRepository,
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
            val babyStatus = babyStatusRepository.getById(id = id)
            babyStatus?.let {
                title = it.title
                height = measureWithLocalDecimal(it.height.toString())
                weight = measureWithLocalDecimal(it.weight.toString())
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
                weight = stringToFloat(weight),
            )
            babyStatusRepository.update(babyStatus)
        }
    }
}
