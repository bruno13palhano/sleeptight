package com.bruno13palhano.sleeptight.ui.screens.analytics

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.di.NapRep
import com.bruno13palhano.core.repository.NapRepository
import com.bruno13palhano.model.Day
import com.bruno13palhano.model.Month
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AnalyticsNapChartViewModel @Inject constructor(
    @NapRep private val napRepository: NapRepository,
    private val stringResourceProvider: StringResourceProvider,
) : ViewModel() {

    val allNapChartUi = napRepository.getAll()
        .map {
            val allSleepTime = mutableListOf<Float>()
            val allDate = mutableListOf<String>()

            it.forEach { nap ->
                allSleepTime.add(timeToDecimal(nap.sleepingTime))
                allDate.add(DateFormatUtil.format(nap.date))
            }

            val chartM = mutableListOf<Pair<String, Float>>()
            for (i in 0 until allDate.size) {
                chartM.add(Pair(allDate[i], allSleepTime[i]))
            }

            ChartEntryModelProducer(
                chartM.mapIndexed { index, (date, y) ->
                    NapChartEntry(date, index.toFloat(), y)
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer(),
        )

    private fun timeToDecimal(time: Long): Float {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = time

        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val finalMinutes: String
        val currentMinutes = minute * 100 / 60
        finalMinutes = if (currentMinutes < 10) {
            "0$currentMinutes"
        } else {
            currentMinutes.toString()
        }

        val timeDecimal = "$hour.$finalMinutes".toFloat()
        return if (timeDecimal == 0.0F) {
            0.0F
        } else {
            String.format("%.2f", timeDecimal)
                .replace(",", ".").toFloat()
        }
    }

    val monthNapChartUi = napRepository.getAll()
        .map {
            val januaryHours = mutableListOf<Int>()
            val januaryMinutes = mutableListOf<Int>()
            val februaryHours = mutableListOf<Int>()
            val februaryMinutes = mutableListOf<Int>()
            val marchHours = mutableListOf<Int>()
            val marchMinutes = mutableListOf<Int>()
            val aprilHours = mutableListOf<Int>()
            val aprilMinutes = mutableListOf<Int>()
            val mayHours = mutableListOf<Int>()
            val mayMinutes = mutableListOf<Int>()
            val juneHours = mutableListOf<Int>()
            val juneMinutes = mutableListOf<Int>()
            val julyHours = mutableListOf<Int>()
            val julyMinutes = mutableListOf<Int>()
            val augustHours = mutableListOf<Int>()
            val augustMinutes = mutableListOf<Int>()
            val septemberHours = mutableListOf<Int>()
            val septemberMinutes = mutableListOf<Int>()
            val octoberHours = mutableListOf<Int>()
            val octoberMinutes = mutableListOf<Int>()
            val novemberHours = mutableListOf<Int>()
            val novemberMinutes = mutableListOf<Int>()
            val decemberHours = mutableListOf<Int>()
            val decemberMinutes = mutableListOf<Int>()

            it.forEach { nap ->
                when (whichMonth(nap.date)) {
                    Month.JANUARY -> {
                        januaryHours.add(hourToInt(nap.sleepingTime))
                        januaryMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.FEBRUARY -> {
                        februaryHours.add(hourToInt(nap.sleepingTime))
                        februaryMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.MARCH -> {
                        marchHours.add(hourToInt(nap.sleepingTime))
                        marchMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.APRIL -> {
                        aprilHours.add(hourToInt(nap.sleepingTime))
                        aprilMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.MAY -> {
                        mayHours.add(hourToInt(nap.sleepingTime))
                        mayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.JUNE -> {
                        juneHours.add(hourToInt(nap.sleepingTime))
                        juneMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.JULY -> {
                        julyHours.add(hourToInt(nap.sleepingTime))
                        julyMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.AUGUST -> {
                        augustHours.add(hourToInt(nap.sleepingTime))
                        augustMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.SEPTEMBER -> {
                        septemberHours.add(hourToInt(nap.sleepingTime))
                        septemberMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.OCTOBER -> {
                        octoberHours.add(hourToInt(nap.sleepingTime))
                        octoberMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.NOVEMBER -> {
                        novemberHours.add(hourToInt(nap.sleepingTime))
                        novemberMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Month.DECEMBER -> {
                        decemberHours.add(hourToInt(nap.sleepingTime))
                        decemberMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                }
            }

            val chartM = mutableListOf<Pair<String, Float>>()
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.january_label),
                    averageSleepTimeDecimal(januaryHours, januaryMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.february_label),
                    averageSleepTimeDecimal(februaryHours, februaryMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.march_label),
                    averageSleepTimeDecimal(marchHours, marchMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.april_label),
                    averageSleepTimeDecimal(aprilHours, aprilMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.may_label),
                    averageSleepTimeDecimal(mayHours, mayMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.june_label),
                    averageSleepTimeDecimal(juneHours, juneMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.july_label),
                    averageSleepTimeDecimal(julyHours, julyMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.august_label),
                    averageSleepTimeDecimal(augustHours, augustMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.september_label),
                    averageSleepTimeDecimal(septemberHours, septemberMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.october_label),
                    averageSleepTimeDecimal(octoberHours, octoberMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.november_label),
                    averageSleepTimeDecimal(novemberHours, novemberMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.december_label),
                    averageSleepTimeDecimal(decemberHours, decemberMinutes),
                ),
            )

            ChartEntryModelProducer(
                chartM.mapIndexed { index, (month, y) ->
                    NapChartEntry(month, index.toFloat(), y)
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer(),
        )

    val weekNapChartUi = napRepository.getAll()
        .map {
            val sundayHours = mutableListOf<Int>()
            val sundayMinutes = mutableListOf<Int>()
            val mondayHours = mutableListOf<Int>()
            val mondayMinutes = mutableListOf<Int>()
            val tuesdayHours = mutableListOf<Int>()
            val tuesdayMinute = mutableListOf<Int>()
            val wednesdayHours = mutableListOf<Int>()
            val wednesdayMinute = mutableListOf<Int>()
            val thursdayHours = mutableListOf<Int>()
            val thursdayMinute = mutableListOf<Int>()
            val fridayHours = mutableListOf<Int>()
            val fridayMinutes = mutableListOf<Int>()
            val saturdayHours = mutableListOf<Int>()
            val saturdayMinutes = mutableListOf<Int>()

            it.forEach { nap ->
                when (whichDay(nap.date)) {
                    Day.SUNDAY -> {
                        sundayHours.add(hourToInt(nap.sleepingTime))
                        sundayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.MONDAY -> {
                        mondayHours.add(hourToInt(nap.sleepingTime))
                        mondayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.TUESDAY -> {
                        tuesdayHours.add(hourToInt(nap.sleepingTime))
                        tuesdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.WEDNESDAY -> {
                        wednesdayHours.add(hourToInt(nap.sleepingTime))
                        wednesdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.THURSDAY -> {
                        thursdayHours.add(hourToInt(nap.sleepingTime))
                        thursdayMinute.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.FRIDAY -> {
                        fridayHours.add(hourToInt(nap.sleepingTime))
                        fridayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                    Day.SATURDAY -> {
                        saturdayHours.add(hourToInt(nap.sleepingTime))
                        saturdayMinutes.add(minuteToInt(nap.sleepingTime))
                    }
                }
            }

            val chartM = mutableListOf<Pair<String, Float>>()
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.sunday_label),
                    averageSleepTimeDecimal(sundayHours, sundayMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.monday_label),
                    averageSleepTimeDecimal(mondayHours, mondayMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.tuesday_label),
                    averageSleepTimeDecimal(tuesdayHours, tuesdayMinute),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.wednesday_label),
                    averageSleepTimeDecimal(wednesdayHours, wednesdayMinute),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.thursday_label),
                    averageSleepTimeDecimal(thursdayHours, thursdayMinute),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.friday_label),
                    averageSleepTimeDecimal(fridayHours, fridayMinutes),
                ),
            )
            chartM.add(
                Pair(
                    stringResourceProvider.getString(R.string.saturday_label),
                    averageSleepTimeDecimal(saturdayHours, saturdayMinutes),
                ),
            )

            ChartEntryModelProducer(
                chartM.mapIndexed { index, (weekday, y) ->
                    NapChartEntry(weekday, index.toFloat(), y)
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer(),
        )

    private var sundayHoursNight = mutableListOf<Int>()
    private var sundayMinutesNight = mutableListOf<Int>()
    private var sundayHoursDay = mutableListOf<Int>()
    private var sundayMinutesDay = mutableListOf<Int>()
    private var mondayHoursNight = mutableListOf<Int>()
    private var mondayMinutesNight = mutableListOf<Int>()
    private var mondayHoursDay = mutableListOf<Int>()
    private var mondayMinutesDay = mutableListOf<Int>()
    private var tuesdayHoursNight = mutableListOf<Int>()
    private var tuesdayMinutesNight = mutableListOf<Int>()
    private var tuesdayHoursDay = mutableListOf<Int>()
    private var tuesdayMinutesDay = mutableListOf<Int>()
    private var wednesdayHoursNight = mutableListOf<Int>()
    private var wednesdayMinutesNight = mutableListOf<Int>()
    private var wednesdayHoursDay = mutableListOf<Int>()
    private var wednesdayMinutesDay = mutableListOf<Int>()
    private var thursdayHoursNight = mutableListOf<Int>()
    private var thursdayMinutesNight = mutableListOf<Int>()
    private var thursdayHoursDay = mutableListOf<Int>()
    private var thursdayMinutesDay = mutableListOf<Int>()
    private var fridayHoursNight = mutableListOf<Int>()
    private var fridayMinutesNight = mutableListOf<Int>()
    private var fridayHoursDay = mutableListOf<Int>()
    private var fridayMinutesDay = mutableListOf<Int>()
    private var saturdayHoursNight = mutableListOf<Int>()
    private var saturdayMinutesNight = mutableListOf<Int>()
    private var saturdayHoursDay = mutableListOf<Int>()
    private var saturdayMinutesDay = mutableListOf<Int>()

    val shiftNapChartUi = napRepository.getAll()
        .map {
            it.forEach { nap ->
                when (whichDay(nap.date)) {
                    Day.SUNDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = sundayHoursDay,
                            nightHours = sundayHoursNight,
                            dayMinutes = sundayMinutesDay,
                            nightMinutes = sundayMinutesNight,
                        )
                    }
                    Day.MONDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = mondayHoursDay,
                            nightHours = mondayHoursNight,
                            dayMinutes = mondayMinutesDay,
                            nightMinutes = mondayMinutesNight,
                        )
                    }
                    Day.TUESDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = tuesdayHoursDay,
                            nightHours = tuesdayHoursNight,
                            dayMinutes = tuesdayMinutesDay,
                            nightMinutes = tuesdayMinutesNight,
                        )
                    }
                    Day.WEDNESDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = wednesdayHoursDay,
                            nightHours = wednesdayHoursNight,
                            dayMinutes = wednesdayMinutesDay,
                            nightMinutes = wednesdayMinutesNight,
                        )
                    }
                    Day.THURSDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = thursdayHoursDay,
                            nightHours = thursdayHoursNight,
                            dayMinutes = thursdayMinutesDay,
                            nightMinutes = thursdayMinutesNight,
                        )
                    }
                    Day.FRIDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = fridayHoursDay,
                            nightHours = fridayHoursNight,
                            dayMinutes = fridayMinutesDay,
                            nightMinutes = fridayMinutesNight,
                        )
                    }
                    Day.SATURDAY -> {
                        setShiftHourAndMinute(
                            startTime = nap.startTime,
                            sleepTime = nap.sleepingTime,
                            dayHours = saturdayHoursDay,
                            nightHours = saturdayHoursNight,
                            dayMinutes = saturdayMinutesDay,
                            nightMinutes = saturdayMinutesNight,
                        )
                    }
                }
            }

            val chartMDay = mutableListOf<Pair<String, Float>>()
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.sunday_label),
                    averageSleepTimeDecimal(sundayHoursDay, sundayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.monday_label),
                    averageSleepTimeDecimal(mondayHoursDay, mondayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.tuesday_label),
                    averageSleepTimeDecimal(tuesdayHoursDay, tuesdayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.wednesday_label),
                    averageSleepTimeDecimal(wednesdayHoursDay, wednesdayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.thursday_label),
                    averageSleepTimeDecimal(thursdayHoursDay, thursdayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.friday_label),
                    averageSleepTimeDecimal(fridayHoursDay, fridayMinutesDay),
                ),
            )
            chartMDay.add(
                Pair(
                    stringResourceProvider.getString(R.string.saturday_label),
                    averageSleepTimeDecimal(saturdayHoursDay, saturdayMinutesDay),
                ),
            )

            val chartMNight = mutableListOf<Pair<String, Float>>()
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.sunday_label),
                    averageSleepTimeDecimal(sundayHoursNight, sundayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.monday_label),
                    averageSleepTimeDecimal(mondayHoursNight, mondayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.tuesday_label),
                    averageSleepTimeDecimal(tuesdayHoursNight, tuesdayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.wednesday_label),
                    averageSleepTimeDecimal(wednesdayHoursNight, wednesdayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.thursday_label),
                    averageSleepTimeDecimal(thursdayHoursNight, thursdayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.friday_label),
                    averageSleepTimeDecimal(fridayHoursNight, fridayMinutesNight),
                ),
            )
            chartMNight.add(
                Pair(
                    stringResourceProvider.getString(R.string.saturday_label),
                    averageSleepTimeDecimal(saturdayHoursNight, saturdayMinutesNight),
                ),
            )

            ChartEntryModelProducer(
                chartMDay.mapIndexed { index, (weekday, y) ->
                    NapChartEntry(weekday, index.toFloat(), y)
                },
                chartMNight.mapIndexed { index, (weekday, y) ->
                    NapChartEntry(weekday, index.toFloat(), y)
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer(),
        )

    private fun setShiftHourAndMinute(
        startTime: Long,
        sleepTime: Long,
        dayHours: MutableList<Int>,
        nightHours: MutableList<Int>,
        dayMinutes: MutableList<Int>,
        nightMinutes: MutableList<Int>,
    ) {
        if (isStartTimeAtNight(startTime)) {
            nightHours.add(hourToInt(sleepTime))
            nightMinutes.add(minuteToInt(sleepTime))
        } else {
            dayHours.add(hourToInt(sleepTime))
            dayMinutes.add(minuteToInt(sleepTime))
        }
    }
}
