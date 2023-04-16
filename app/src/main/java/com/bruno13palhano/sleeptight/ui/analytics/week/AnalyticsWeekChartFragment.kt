package com.bruno13palhano.sleeptight.ui.analytics.week

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsWeekChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsWeekChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsWeekChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsWeekChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_week_chart, container, false)
        val view = binding.root

        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
            .title(getString(R.string.week_chart_label))
            .titleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .subtitle(getString(R.string.average_hour_per_week_label))
            .subtitleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .dataLabelsEnabled(true)
            .backgroundColor(getString(R.string.chart_background_color))
            .axesTextColor(getString(R.string.chart_primary_color))
            .dataLabelsStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name(getString(R.string.average_hours_label))
                        .data(arrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F))
                        .color(getString(R.string.chart_bar_main_color))
                )
            ).categories(
                arrayOf(
                    getString(R.string.sunday_label),
                    getString(R.string.monday_label),
                    getString(R.string.tuesday_label),
                    getString(R.string.wednesday_label),
                    getString(R.string.thursday_label),
                    getString(R.string.friday_label),
                    getString(R.string.saturday_label)
                )
            )
        binding.weekChart.aa_drawChartWithChartModel(chartModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chartUi.collect {
                    delay(200)
                    binding.weekChart.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
                        arrayOf(
                            AASeriesElement()
                                .name(getString(R.string.average_hours_label))
                                .data(
                                    arrayOf(
                                        it.sunday,
                                        it.monday,
                                        it.tuesday,
                                        it.wednesday,
                                        it.thursday,
                                        it.friday,
                                        it.saturday
                                    )
                                )
                                .color(getString(R.string.chart_bar_main_color))
                        )
                    )
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}