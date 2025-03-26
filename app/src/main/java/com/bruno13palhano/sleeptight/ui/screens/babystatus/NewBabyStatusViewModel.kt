package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.CommonDataContract
import com.bruno13palhano.core.di.BabyStatusRep
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NewBabyStatusViewModel @Inject constructor(
    @BabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>,
) : ViewModel() {

    var dateInMillis by mutableLongStateOf(System.currentTimeMillis())
        private set
    var date by mutableStateOf(DateFormatUtil.format(dateInMillis))
        private set
    var title by mutableStateOf("")
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

    fun insertBabyStatus() {
        val babyStatus = BabyStatus(
            id = 0L,
            title = title,
            date = dateInMillis,
            height = stringToFloat(height),
            weight = stringToFloat(weight),
        )

        viewModelScope.launch {
            babyStatusRepository.insert(babyStatus)
        }
        restoresValues()
    }

    private fun restoresValues() {
        dateInMillis = System.currentTimeMillis()
        title = ""
        height = ""
        weight = ""
    }
}
