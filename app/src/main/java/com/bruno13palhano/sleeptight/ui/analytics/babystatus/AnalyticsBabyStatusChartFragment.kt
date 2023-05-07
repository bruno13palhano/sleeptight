package com.bruno13palhano.sleeptight.ui.analytics.babystatus

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentAnalyticsBabyStatusChartBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint

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

        val chartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Height and Weight")
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Height")
                        .data(arrayOf(2.3F)),
                    AASeriesElement()
                        .name("Weight")
                        .data(arrayOf(4.5))
                )
            ).categories(arrayOf("Average height and weight"))

        binding.babyStatusChart.aa_drawChartWithChartModel(chartModel)

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