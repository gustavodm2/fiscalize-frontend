package com.example.fiscalize.viewModel

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.fiscalize.R
import com.example.fiscalize.model.PieChartData
import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.ui.theme.blueColor
import com.example.fiscalize.ui.theme.greenColor
import com.example.fiscalize.ui.theme.redColor
import com.example.fiscalize.ui.theme.yellowColor
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.random.Random

fun updatePieChartWithData(
    chart: PieChart,
    data: List<PieChartData>,
    context: Context
) {



    val entries = ArrayList<PieEntry>()



    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.value ?: 0.toFloat(), item.browserName ?: ""))
    }



    val ds = PieDataSet(entries, "")

    val randomColors = entries.map { getRandomColor() }
    ds.colors = ArrayList(randomColors)

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