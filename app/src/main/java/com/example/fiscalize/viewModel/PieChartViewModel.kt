package com.example.fiscalize.viewModel

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import com.example.fiscalize.model.PieChartData
import com.example.fiscalize.model.documents.FilteredTaxes
import com.example.fiscalize.model.documents.SimplesModel
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

    for (simplesModel in data) {
            val totalValue = simplesModel.total
            val denomination = simplesModel.denomination.split(" ")[0]
            entries.add(PieEntry(totalValue, denomination))
    }

    val ds = PieDataSet(entries, "")

    val randomColors = entries.map { getRandomColor() }
    ds.colors = ArrayList(randomColors)

    ds.setDrawValues(false)


    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.sliceSpace = 2f
    ds.valueTextSize = 18f
    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val pieData = PieData(ds)
    chart.data = pieData
    chart.invalidate()



}

private fun getRandomColor(): Int {
    val random = Random
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}