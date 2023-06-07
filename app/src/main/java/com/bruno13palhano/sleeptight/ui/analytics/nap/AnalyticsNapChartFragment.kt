package com.bruno13palhano.sleeptight.ui.analytics.nap

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsNapChartBinding
import com.bruno13palhano.sleeptight.ui.analytics.shareChart
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

        (activity as MainActivity).supportActionBar?.title = getString(R.string.naps_chart_label)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNapChartUi.collect {
                    setAllNapChart(it)
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.chart_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.all_nap_chart -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.allNapChartUi.collect {
                                setAllNapChart(it)
                            }
                        }
                        true
                    }
                    R.id.month_nap_chart -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.monthNapChartUi.collect {
                                setMonthNapChart(it)
                            }
                        }
                        true
                    }
                    R.id.week_nap_chart -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.weekNapChartUi.collect {
                                setWeekNapChart(it)
                            }
                        }
                        true
                    }
                    R.id.shift_nap_chart -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.shiftNapChartUi.collect {
                                setShiftNapChart(it)
                            }
                        }
                        true
                    }
                    R.id.share -> {
                        shareChart(
                            context = this@AnalyticsNapChartFragment.requireContext(),
                            chartName = getString(R.string.naps_chart_label),
                            view = binding.napChart,
                            height = binding.napChart.height,
                            width = binding.napChart.width
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAllNapChart(allNapChart: AnalyticsNapChartViewModel.AllNapChartUi) {
        val option = AAOptions()
            .xAxis(AAXAxis()
                .type(AAChartAxisType.Datetime)
                .categories(allNapChart.allDate.toTypedArray())
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
                        .data(allNapChart.allSleeptime.toTypedArray())
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

    private fun setMonthNapChart(monthNapChart: AnalyticsNapChartViewModel.MonthNapChartUi) {
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
                        .name(getString(R.string.january_label))
                        .data(arrayOf(monthNapChart.january))
                        .color(getString(R.string.january_light_blue_label)),
                    AASeriesElement()
                        .name(getString(R.string.february_label))
                        .data(arrayOf(monthNapChart.february))
                        .color(getString(R.string.february_pink_label)),
                    AASeriesElement()
                        .name(getString(R.string.march_label))
                        .data(arrayOf(monthNapChart.march))
                        .color(getString(R.string.march_purple_label)),
                    AASeriesElement()
                        .name(getString(R.string.april_label))
                        .data(arrayOf(monthNapChart.april))
                        .color(getString(R.string.april_grass_green_label)),
                    AASeriesElement()
                        .name(getString(R.string.may_label))
                        .data(arrayOf(monthNapChart.may))
                        .color(getString(R.string.may_lilac_label)),
                    AASeriesElement()
                        .name(getString(R.string.june_label))
                        .data(arrayOf(monthNapChart.june))
                        .color(getString(R.string.june_pearl_label)),
                    AASeriesElement()
                        .name(getString(R.string.july_label))
                        .data(arrayOf(monthNapChart.july))
                        .color(getString(R.string.july_yellow_label)),
                    AASeriesElement()
                        .name(getString(R.string.august_label))
                        .data(arrayOf(monthNapChart.august))
                        .color(getString(R.string.august_orange_label)),
                    AASeriesElement()
                        .name(getString(R.string.september_label))
                        .data(arrayOf(monthNapChart.september))
                        .color(getString(R.string.september_bright_blue_label)),
                    AASeriesElement()
                        .name(getString(R.string.october_label))
                        .data(arrayOf(monthNapChart.october))
                        .color(getString(R.string.october_indigo_label)),
                    AASeriesElement()
                        .name(getString(R.string.november_label))
                        .data(arrayOf(monthNapChart.november))
                        .color(getString(R.string.november_gold_label)),
                    AASeriesElement()
                        .name(getString(R.string.december_label))
                        .data(arrayOf(monthNapChart.december))
                        .color(getString(R.string.december_dark_red_label))
                )
            ).categories(arrayOf(getString(R.string.average_hours_label)))

        binding.napChart.aa_drawChartWithChartModel(chartModel)
    }

    private fun setWeekNapChart(weekNapChart: AnalyticsNapChartViewModel.WeekNapChartUi) {
        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
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
                        .name(getString(R.string.sunday_label))
                        .data(arrayOf(weekNapChart.sunday))
                        .color(getString(R.string.january_light_blue_label)),
                    AASeriesElement()
                        .name(getString(R.string.monday_label))
                        .data(arrayOf(weekNapChart.monday))
                        .color(getString(R.string.july_yellow_label)),
                    AASeriesElement()
                        .name(getString(R.string.tuesday_label))
                        .data(arrayOf(weekNapChart.tuesday))
                        .color(getString(R.string.august_orange_label)),
                    AASeriesElement()
                        .name(getString(R.string.wednesday_label))
                        .data(arrayOf(weekNapChart.wednesday))
                        .color(getString(R.string.september_bright_blue_label)),
                    AASeriesElement()
                        .name(getString(R.string.thursday_label))
                        .data(arrayOf(weekNapChart.thursday))
                        .color(getString(R.string.october_indigo_label)),
                    AASeriesElement()
                        .name(getString(R.string.friday_label))
                        .data(arrayOf(weekNapChart.friday))
                        .color(getString(R.string.november_gold_label)),
                    AASeriesElement()
                        .name(getString(R.string.saturday_label))
                        .data(arrayOf(weekNapChart.saturday))
                        .color(getString(R.string.december_dark_red_label)),
                )
            ).categories(arrayOf(getString(R.string.average_hours_label)))

        binding.napChart.aa_drawChartWithChartModel(chartModel)
    }

    private fun setShiftNapChart(shiftNapChartUi: AnalyticsNapChartViewModel.ShiftNapChartUi) {
        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title(getString(R.string.shift_chart_label))
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
                        .name(getString(R.string.day_label))
                        .data(
                            arrayOf(
                                shiftNapChartUi.day.sunday,
                                shiftNapChartUi.day.monday,
                                shiftNapChartUi.day.tuesday,
                                shiftNapChartUi.day.wednesday,
                                shiftNapChartUi.day.thursday,
                                shiftNapChartUi.day.friday,
                                shiftNapChartUi.day.saturday
                            )
                        )
                        .color(getString(R.string.january_light_blue_label)),
                    AASeriesElement()
                        .name(getString(R.string.night_label))
                        .data(
                            arrayOf(
                                shiftNapChartUi.night.sunday,
                                shiftNapChartUi.night.monday,
                                shiftNapChartUi.night.tuesday,
                                shiftNapChartUi.night.wednesday,
                                shiftNapChartUi.night.thursday,
                                shiftNapChartUi.night.friday,
                                shiftNapChartUi.night.saturday
                            )
                        )
                        .color(getString(R.string.july_yellow_label))
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

        binding.napChart.aa_drawChartWithChartModel(chartModel)
    }
}