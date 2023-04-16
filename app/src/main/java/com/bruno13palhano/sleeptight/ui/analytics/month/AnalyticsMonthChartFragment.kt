package com.bruno13palhano.sleeptight.ui.analytics.month

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
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsMonthChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsMonthChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsMonthChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsMonthChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_month_chart, container, false)
        val view = binding.root

        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title(getString(R.string.month_chart_label))
            .titleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .subtitle(getString(R.string.average_hour_per_month_label))
            .subtitleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .dataLabelsEnabled(true)
            .backgroundColor(getString(R.string.chart_background_color))
            .axesTextColor(getString(R.string.chart_primary_color))
            .dataLabelsStyle(AAStyle().color(getString(R.string.chart_primary_color)))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name(getString(R.string.average_hours_label))
                        .data(arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
                        .color(getString(R.string.chart_bar_main_color)),
                )
            ).categories(
                arrayOf(
                    getString(R.string.january_label),
                    getString(R.string.february_label),
                    getString(R.string.march_label),
                    getString(R.string.april_label),
                    getString(R.string.may_label),
                    getString(R.string.june_label),
                    getString(R.string.july_label),
                    getString(R.string.august_label),
                    getString(R.string.september_label),
                    getString(R.string.october_label),
                    getString(R.string.november_label),
                    getString(R.string.december_label)
                )
            )

        binding.monthChart.aa_drawChartWithChartModel(chartModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chartUi.collect {
                    delay(200)
                    binding.monthChart.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
                        arrayOf(
                            AASeriesElement()
                                .name(getString(R.string.month_chart_label))
                                .data(arrayOf(
                                    it.january,
                                    it.february,
                                    it.march,
                                    it.april,
                                    it.may,
                                    it.june,
                                    it.july,
                                    it.august,
                                    it.september,
                                    it.october,
                                    it.november,
                                    it.december
                                ))
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