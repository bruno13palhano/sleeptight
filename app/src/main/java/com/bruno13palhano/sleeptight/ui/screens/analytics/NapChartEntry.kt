package com.bruno13palhano.sleeptight.ui.screens.analytics

import com.patrykandpatrick.vico.core.entry.ChartEntry

/**
 * Custom ChartEntry
 *
 * This is a custom implementation of ChartEntry from vico chart library
 * to handle string label in the x axis.
 *
 * @property date a string to represent the date in x axis label.
 * @property x the index value of x axis.
 * @property y the value of y axis.
 */
class NapChartEntry(
    val date: String,
    override val x: Float,
    override val y: Float
) : ChartEntry {
    override fun withY(y: Float): ChartEntry =
        NapChartEntry(date, x, y)
}