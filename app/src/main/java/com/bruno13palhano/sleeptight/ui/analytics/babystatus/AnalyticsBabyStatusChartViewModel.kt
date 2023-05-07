package com.bruno13palhano.sleeptight.ui.analytics.babystatus

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsBabyStatusChartViewModel @Inject constructor(
    @DefaultBabyStatusRep babyStatusRepository: BabyStatusRepository
) : ViewModel() {

}