package com.example.fiscalize.activities

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fiscalize.R
import com.example.fiscalize.model.getPieChartData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.example.fiscalize.viewModel.SimplesViewModel
import com.example.fiscalize.viewModel.updatePieChartWithData


@SuppressLint("SuspiciousIndentation")
@Composable
fun SimplesActivity(modifier: Modifier = Modifier, navController: NavHostController){
     // on below line we are creating a
     // pie chart function on below line.


     val simplesViewModel: SimplesViewModel = viewModel()
     val filteredTaxes = simplesViewModel.filteredTaxes
     val simplesDocument = simplesViewModel.simplesNacional
     val context = LocalContext.current


     LaunchedEffect(Unit) {
          simplesViewModel.filterDocuments()
          simplesViewModel.getDocuments()
     }


     Log.d("graf", "$filteredTaxes")
     Log.d("graf", "$simplesDocument")

          Column(modifier = Modifier.fillMaxSize()) {
               Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
               ) {
                    Text(
                         text = "Impostos",
                         style = TextStyle(
                              fontFamily = FontFamily.Default,
                              fontStyle = FontStyle.Normal,
                              fontSize = 20.sp

                         ),
                         fontFamily = FontFamily.Default,
                         fontStyle = FontStyle.Normal,
                         fontSize = 20.sp
                    )

                    Column(
                         modifier = Modifier
                              .padding(18.dp)
                              .size(320.dp),
                         horizontalAlignment = Alignment.CenterHorizontally,
                         verticalArrangement = Arrangement.Center
                    ) {
                         Crossfade(targetState = filteredTaxes) { pieChartData ->
                              AndroidView(factory = { context ->
                                   PieChart(context).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                             ViewGroup.LayoutParams.MATCH_PARENT,
                                             ViewGroup.LayoutParams.MATCH_PARENT,
                                        )
                                        this.description.isEnabled = false
                                        this.isDrawHoleEnabled = true
                                        this.legend.isEnabled = true
                                        this.legend.textSize = 14F
                                        this.legend.horizontalAlignment =
                                             Legend.LegendHorizontalAlignment.CENTER
                                        ContextCompat.getColor(context, R.color.white)
                                   }
                              },
                                   modifier = Modifier
                                        .wrapContentSize()
                                        .padding(5.dp), update = {
                                        updatePieChartWithData(it, pieChartData, context)
                                   })
                         }
                    }
               }
          }



}