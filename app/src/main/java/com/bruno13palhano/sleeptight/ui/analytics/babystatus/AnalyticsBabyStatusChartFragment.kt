package com.bruno13palhano.sleeptight.ui.analytics.babystatus

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
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsBabyStatusChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsBabyStatusChartFragment : Fragment() {
    private var _binding: FragmentAnalyticsBabyStatusChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsBabyStatusChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_analytics_baby_status_chart, container, false)
        val view = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.babyStatusChartUi.collect {
                    val chartModel: AAChartModel = AAChartModel()
                        .chartType(AAChartType.Column)
                        .title(getString(R.string.baby_status_height_and_weight))
                        .titleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
                        .subtitle(getString(R.string.baby_development_label))
                        .subtitleStyle(AAStyle().color(getString(R.string.chart_primary_color)))
                        .dataLabelsEnabled(true)
                        .backgroundColor(getString(R.string.chart_background_color))
                        .axesTextColor(getString(R.string.chart_primary_color))
                        .dataLabelsStyle(AAStyle().color(getString(R.string.chart_primary_color)))
                        .series(
                            arrayOf(
                                AASeriesElement()
                                    .name(getString(R.string.birth_height_label))
                                    .data(it.allHeight.toTypedArray())
                                    .color(getString(R.string.chart_bar_main_color)),
                                AASeriesElement()
                                    .name(getString(R.string.birth_weight_label))
                                    .data(it.allWeight.toTypedArray())
                                    .color(getString(R.string.chart_bar_second_color))
                            )
                        )

                    binding.babyStatusChart.aa_drawChartWithChartModel(chartModel)
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