package com.bruno13palhano.sleeptight.ui.analytics.week

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
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsWeekChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chartUi.collect {
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
                                    .data(arrayOf(it.sunday))
                                    .color(getString(R.string.january_light_blue_label)),
                                AASeriesElement()
                                    .name(getString(R.string.monday_label))
                                    .data(arrayOf(it.monday))
                                    .color(getString(R.string.july_yellow_label)),
                                AASeriesElement()
                                    .name(getString(R.string.tuesday_label))
                                    .data(arrayOf(it.tuesday))
                                    .color(getString(R.string.august_orange_label)),
                                AASeriesElement()
                                    .name(getString(R.string.wednesday_label))
                                    .data(arrayOf(it.wednesday))
                                    .color(getString(R.string.september_bright_blue_label)),
                                AASeriesElement()
                                    .name(getString(R.string.thursday_label))
                                    .data(arrayOf(it.thursday))
                                    .color(getString(R.string.october_indigo_label)),
                                AASeriesElement()
                                    .name(getString(R.string.friday_label))
                                    .data(arrayOf(it.friday))
                                    .color(getString(R.string.november_gold_label)),
                                AASeriesElement()
                                    .name(getString(R.string.saturday_label))
                                    .data(arrayOf(it.saturday))
                                    .color(getString(R.string.december_dark_red_label)),
                            )
                        ).categories(arrayOf(getString(R.string.average_hours_label)))

                    binding.weekChart.aa_drawChartWithChartModel(chartModel)
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
                menuInflater.inflate(R.menu.share_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
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
}