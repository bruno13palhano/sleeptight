package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.BabyStatusRep
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBabyStatusViewModel @Inject constructor(
    @BabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>
) : ViewModel() {

    var dateInMillis by mutableLongStateOf(MaterialDatePicker.todayInUtcMilliseconds())
        private set
    var date by mutableStateOf(DateFormatUtil.format(dateInMillis))
        private set
    var title by mutableStateOf("")
       private set
    var height by mutableStateOf("")
        private set
    var weight by mutableStateOf("")
        private set

    val isTitleNotEmpty = title.trim() != ""
    val heightAndWeightValue = height.trim() != "" && weight.trim() != ""

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

    fun insertBabyStatus() {
        viewModelScope.launch {
            val babyStatus = BabyStatus(
                id = 0L,
                title = title,
                date = dateInMillis,
                height = stringToFloat(height),
                weight = stringToFloat(weight)
            )
            babyStatusRepository.insert(babyStatus)
        }
        restoresValues()
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) { 0F }
    }

    private fun restoresValues() {
        dateInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        title = ""
        height = ""
        weight = ""
    }
}