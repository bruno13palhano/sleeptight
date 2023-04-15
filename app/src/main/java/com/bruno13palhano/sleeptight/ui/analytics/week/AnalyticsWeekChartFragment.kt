package com.bruno13palhano.sleeptight.ui.analytics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsWeekChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

class AnalyticsWeekChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsWeekChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_week_chart, container, false)
        val view = binding.root

        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Week Chart")
            .dataLabelsEnabled(true)
            .backgroundColor(getString(R.string.chart_background_color))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Sample 1")
                        .data(arrayOf(1.2, 2.2, 3.3, 4.4))
                        .color("#786e82"),
                    AASeriesElement()
                        .name("Sample 2")
                        .data(arrayOf(4.2, 3.4, 2.3, 1.2))
                        .color("#79c5c8")
                )
            ).categories(arrayOf("Samples"))

        binding.weekChart.aa_drawChartWithChartModel(chartModel)

        return view
    }
}