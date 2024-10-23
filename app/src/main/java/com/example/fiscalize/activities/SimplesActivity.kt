package com.example.fiscalize.activities

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fiscalize.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.example.fiscalize.viewModel.SimplesViewModel
import com.example.fiscalize.viewModel.updatePieChartWithData
import com.example.fiscalize.components.TaxCard


@SuppressLint("SuspiciousIndentation")
@Composable
fun SimplesActivity(modifier: Modifier = Modifier, navController: NavHostController) {
     val simplesViewModel: SimplesViewModel = viewModel()
     val context = LocalContext.current

     LaunchedEffect(Unit) {
          simplesViewModel.getDocuments()
     }

     Column(modifier = Modifier.fillMaxSize()) {
          Text(
               text = "Impostos",
               style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
               ),
               modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
          )

          // Gráfico de Pizza
          Column(
               modifier = Modifier
                    .padding(18.dp)
                    .size(320.dp)
                    .align(Alignment.CenterHorizontally),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
          ) {
               Crossfade(targetState = simplesViewModel.filteredTaxes) { pieChartData ->
                    AndroidView(factory = { context ->
                         PieChart(context).apply {
                              layoutParams = LinearLayout.LayoutParams(
                                   ViewGroup.LayoutParams.MATCH_PARENT,
                                   ViewGroup.LayoutParams.MATCH_PARENT,
                              )
                              this.description.isEnabled = false
                              this.isDrawHoleEnabled = true
                              // Desabilita a legenda
                              this.legend.isEnabled = false
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

          // Verificação se a lista está vazia
          if (simplesViewModel.filteredTaxes.isEmpty()) {
               Text(
                    text = "Nenhum imposto encontrado.",
                    modifier = Modifier
                         .padding(16.dp)
                         .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
               )
          } else {
               // Lista de TaxCards
               LazyColumn(
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(top = 8.dp)
               ) {
                    items(simplesViewModel.filteredTaxes) { tax ->
                         TaxCard(
                              denomination = tax.denomination,
                              totalValue = tax.total,
                              color = tax.color // Passa a cor para o card
                         )
                    }
               }
          }
     }
}







