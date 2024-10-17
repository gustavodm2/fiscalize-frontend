package com.example.fiscalize.activities

import android.annotation.SuppressLint
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
import androidx.navigation.NavHostController
import com.example.fiscalize.R
import com.example.fiscalize.model.getPieChartData
import com.example.fiscalize.viewModel.updatePieChartWithData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend

@SuppressLint("SuspiciousIndentation")
@Composable
fun SimplesActivity(modifier: Modifier = Modifier, navController: NavHostController){
     // on below line we are creating a
     // pie chart function on below line.



     val context = LocalContext.current
          // on below line we are creating a column
          // and specifying a modifier as max size.
          Column(modifier = Modifier.fillMaxSize()) {
               // on below line we are again creating a column
               // with modifier and horizontal and vertical arrangement
               Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
               ) {
                    // on below line we are creating a simple text
                    // and specifying a text as Web browser usage share
                    Text(
                         text = "Web Browser Usage Share",

                         // on below line we are specifying style for our text
                         style = TextStyle(
                              fontFamily = FontFamily.Default,
                              fontStyle = FontStyle.Normal,
                              fontSize = 20.sp

                         ),

                         // on below line we are specifying font family.
                         fontFamily = FontFamily.Default,

                         // on below line we are specifying font style
                         fontStyle = FontStyle.Normal,

                         // on below line we are specifying font size.
                         fontSize = 20.sp
                    )

                    // on below line we are creating a column and
                    // specifying the horizontal and vertical arrangement
                    // and specifying padding from all sides.
                    Column(
                         modifier = Modifier
                              .padding(18.dp)
                              .size(320.dp),
                         horizontalAlignment = Alignment.CenterHorizontally,
                         verticalArrangement = Arrangement.Center
                    ) {
                         // on below line we are creating a cross fade and
                         // specifying target state as pie chart data the
                         // method we have created in Pie chart data class.
                         Crossfade(targetState = getPieChartData) { pieChartData ->
                              // on below line we are creating an
                              // android view for pie chart.
                              AndroidView(factory = { context ->
                                   // on below line we are creating a pie chart
                                   // and specifying layout params.
                                   PieChart(context).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                             // on below line we are specifying layout
                                             // params as MATCH PARENT for height and width.
                                             ViewGroup.LayoutParams.MATCH_PARENT,
                                             ViewGroup.LayoutParams.MATCH_PARENT,
                                        )
                                        // on below line we are setting description
                                        // enables for our pie chart.
                                        this.description.isEnabled = false

                                        // on below line we are setting draw hole
                                        // to false not to draw hole in pie chart
                                        this.isDrawHoleEnabled = false

                                        // on below line we are enabling legend.
                                        this.legend.isEnabled = true

                                        // on below line we are specifying
                                        // text size for our legend.
                                        this.legend.textSize = 14F

                                        // on below line we are specifying
                                        // alignment for our legend.
                                        this.legend.horizontalAlignment =
                                             Legend.LegendHorizontalAlignment.CENTER

                                        // on below line we are specifying entry label color as white.
                                        ContextCompat.getColor(context, R.color.white)
                                        //this.setEntryLabelColor(resources.getColor(R.color.white))
                                   }
                              },
                                   // on below line we are specifying modifier
                                   // for it and specifying padding to it.
                                   modifier = Modifier
                                        .wrapContentSize()
                                        .padding(5.dp), update = {
                                        // on below line we are calling update pie chart
                                        // method and passing pie chart and list of data.
                                        updatePieChartWithData(it, pieChartData, context)
                                   })
                         }
                    }
               }
          }



}