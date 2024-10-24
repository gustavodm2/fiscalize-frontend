package com.example.fiscalize.viewModel

import android.content.Context
import android.graphics.Color
import com.example.fiscalize.model.documents.FilteredTaxes
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.random.Random

fun updatePieChartWithData(
    chart: PieChart,
    data: List<FilteredTaxes>,
    context: Context
) {
    val entries = ArrayList<PieEntry>()
    val colors = ArrayList<Int>()

    for (tax in data) {
        val totalValue = tax.total
        val denomination = tax.denomination.split(" ")[0]
        entries.add(PieEntry(totalValue, denomination))
        colors.add(tax.color)
    }

    val ds = PieDataSet(entries, "")
    ds.colors = colors
    ds.setDrawValues(false) // Não exibe os valores nas fatias

    // Desabilita a posição dos valores nas fatias
    ds.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

    val pieData = PieData(ds)
    chart.data = pieData

    // Não exibe os rótulos nas fatias
    chart.setDrawEntryLabels(false)

    chart.invalidate()
}


