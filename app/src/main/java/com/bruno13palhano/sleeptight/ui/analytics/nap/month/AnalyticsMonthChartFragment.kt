package com.bruno13palhano.sleeptight.ui.analytics.nap.month

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
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsMonthChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chartUi.collect {
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
                                    .data(arrayOf(it.january))
                                    .color(getString(R.string.january_light_blue_label)),
                                AASeriesElement()
                                    .name(getString(R.string.february_label))
                                    .data(arrayOf(it.february))
                                    .color(getString(R.string.february_pink_label)),
                                AASeriesElement()
                                    .name(getString(R.string.march_label))
                                    .data(arrayOf(it.march))
                                    .color(getString(R.string.march_purple_label)),
                                AASeriesElement()
                                    .name(getString(R.string.april_label))
                                    .data(arrayOf(it.april))
                                    .color(getString(R.string.april_grass_green_label)),
                                AASeriesElement()
                                    .name(getString(R.string.may_label))
                                    .data(arrayOf(it.may))
                                    .color(getString(R.string.may_lilac_label)),
                                AASeriesElement()
                                    .name(getString(R.string.june_label))
                                    .data(arrayOf(it.june))
                                    .color(getString(R.string.june_pearl_label)),
                                AASeriesElement()
                                    .name(getString(R.string.july_label))
                                    .data(arrayOf(it.july))
                                    .color(getString(R.string.july_yellow_label)),
                                AASeriesElement()
                                    .name(getString(R.string.august_label))
                                    .data(arrayOf(it.august))
                                    .color(getString(R.string.august_orange_label)),
                                AASeriesElement()
                                    .name(getString(R.string.september_label))
                                    .data(arrayOf(it.september))
                                    .color(getString(R.string.september_bright_blue_label)),
                                AASeriesElement()
                                    .name(getString(R.string.october_label))
                                    .data(arrayOf(it.october))
                                    .color(getString(R.string.october_indigo_label)),
                                AASeriesElement()
                                    .name(getString(R.string.november_label))
                                    .data(arrayOf(it.november))
                                    .color(getString(R.string.november_gold_label)),
                                AASeriesElement()
                                    .name(getString(R.string.december_label))
                                    .data(arrayOf(it.december))
                                    .color(getString(R.string.december_dark_red_label))
                            )
                        ).categories(arrayOf(getString(R.string.average_hours_label)))

                    binding.monthChart.aa_drawChartWithChartModel(chartModel)
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