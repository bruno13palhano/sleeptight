package com.bruno13palhano.sleeptight.ui.analytics.nap.shift

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsShiftChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsShiftChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsShiftChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsShiftChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_shift_chart, container, false)
        val view = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shiftUi.collect {
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
                                            it.day.sunday,
                                            it.day.monday,
                                            it.day.tuesday,
                                            it.day.wednesday,
                                            it.day.thursday,
                                            it.day.friday,
                                            it.day.saturday
                                        )
                                    )
                                    .color(getString(R.string.january_light_blue_label)),
                                AASeriesElement()
                                    .name(getString(R.string.night_label))
                                    .data(
                                        arrayOf(
                                            it.night.sunday,
                                            it.night.monday,
                                            it.night.tuesday,
                                            it.night.wednesday,
                                            it.night.thursday,
                                            it.night.friday,
                                            it.night.saturday
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

                    binding.shiftChart.aa_drawChartWithChartModel(chartModel)
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