package com.bruno13palhano.sleeptight.ui.analytics.nap

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
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsNapChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsNapChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsNapChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsNapChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_nap_chart, container, false)
        val view = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.napChartUi.collect {
                    val option = AAOptions()
                        .xAxis(AAXAxis()
                            .type(AAChartAxisType.Datetime)
                            .categories(it.allDate.toTypedArray())
                            .labels(AALabels()
                                .style(AAStyle()
                                    .color(getString(R.string.chart_primary_color))))
                        )
                        .yAxis(AAYAxis()
                            .title(AATitle().style(AAStyle().color(getString(R.string.chart_primary_color))))
                            .labels(AALabels()
                                .style(AAStyle()
                                    .color(getString(R.string.chart_primary_color))))
                        )
                        .chart(AAChart()
                            .type(AAChartType.Column)
                            .backgroundColor(getString(R.string.chart_background_color))
                        )
                        .title(AATitle()
                            .text(getString(R.string.naps_chart_label))
                            .style(AAStyle().color(getString(R.string.chart_primary_color)))
                        )
                        .subtitle(AASubtitle()
                            .text(getString(R.string.all_naps_chart_label))
                            .style(AAStyle().color(getString(R.string.chart_primary_color)))
                        )
                        .series(
                            arrayOf(
                                AASeriesElement()
                                    .data(it.allSleeptime.toTypedArray())
                                    .name(getString(R.string.nap_label))
                                    .dataLabels(AADataLabels()
                                        .enabled(true)
                                        .style(AAStyle()
                                            .color(getString(R.string.chart_primary_color))
                                        )
                                    )
                                    .borderWidth(0)
                                    .color(getString(R.string.chart_bar_main_color))
                            )
                        )
                        .legend(AALegend()
                            .enabled(true)
                            .itemStyle(AAItemStyle()
                                .color(getString(R.string.chart_primary_color)))
                        )
                    binding.napChart.aa_drawChartWithChartOptions(option)
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