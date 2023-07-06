package com.bruno13palhano.sleeptight.ui.screens.analytics

import com.patrykandpatrick.vico.core.entry.ChartEntry

class NapChartEntry(
    val date: String,
    override val x: Float,
    override val y: Float
) : ChartEntry {
    override fun withY(y: Float): ChartEntry =
        NapChartEntry(date, x, y)
}