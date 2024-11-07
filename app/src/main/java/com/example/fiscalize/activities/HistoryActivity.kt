package com.example.fiscalize.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.fiscalize.components.SimplesCard
import com.example.fiscalize.viewModel.GraphViewModel


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.fiscalize.components.TopBarComponent
import com.example.fiscalize.model.documents.SimplesModel
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HistoryActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainHost: NavController,
    graphViewModel: GraphViewModel
) {
    val simplesDocument = graphViewModel.simplesNacional
    val context: Context = LocalContext.current

    var filteredDocuments by remember { mutableStateOf(simplesDocument) }

    LaunchedEffect(Unit) {
        graphViewModel.getDocuments(context)
    }

    fun applyFilter(filterType: String) {
        val monthMap = mapOf(
            "Janeiro" to 1, "Fevereiro" to 2, "Março" to 3, "Abril" to 4,
            "Maio" to 5, "Junho" to 6, "Julho" to 7, "Agosto" to 8,
            "Setembro" to 9, "Outubro" to 10, "Novembro" to 11, "Dezembro" to 12
        )

        filteredDocuments = when (filterType) {
            "Mais Antigos" -> simplesDocument.sortedBy { doc ->
                parseCalculationPeriod(doc.calculationPeriod, monthMap)
            }
            "Mais Novos" -> simplesDocument.sortedByDescending { doc ->
                parseCalculationPeriod(doc.calculationPeriod, monthMap)
            }
            else -> simplesDocument
        }
    }

    Scaffold(
        topBar = { TopBarComponent() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { applyFilter("Mais Antigos") }) {
                        Text("Mais Antigos")
                    }
                    Button(onClick = { applyFilter("Mais Novos") }) {
                        Text("Mais Novos")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Histórico de documentos",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            items(filteredDocuments) { doc ->
                SimplesCard(doc, navController, graphViewModel, mainHost)
            }
        }
    }
}

fun parseCalculationPeriod(period: String, monthMap: Map<String, Int>): LocalDate? {
    val parts = period.split("/")
    if (parts.size == 2) {
        val month = monthMap[parts[0].capitalize(Locale.ROOT)]
        val year = parts[1].toIntOrNull()
        if (month != null && year != null) {
            return LocalDate.of(year, month, 1)
        }
    }
    return null
}