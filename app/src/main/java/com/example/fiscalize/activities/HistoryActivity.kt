package com.example.fiscalize.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.fiscalize.components.MonthPicker
import com.example.fiscalize.components.TopBarComponent
import com.example.fiscalize.model.documents.SimplesModel
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HistoryActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainHost: NavController,
    graphViewModel: GraphViewModel
) {
    val context: Context = LocalContext.current
    var filteredDocuments by remember { mutableStateOf(graphViewModel.simplesNacional) }

    var visible by remember { mutableStateOf(false) }
    var selectedDateField by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("de") }
    var endDate by remember { mutableStateOf("até") }

    LaunchedEffect(Unit) {
        graphViewModel.getDocuments(context)
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
                    Text(
                        text = startDate,
                        modifier = Modifier
                            .clickable {
                                selectedDateField = "start"
                                visible = true
                            }
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Text(
                        text = endDate,
                        modifier = Modifier
                            .clickable {
                                selectedDateField = "end"
                                visible = true
                            }
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Filtrar",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(16.dp)
                    )
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

        if (visible) {
            MonthPicker(
                visible = visible,
                currentMonth = Calendar.getInstance().get(Calendar.MONTH),
                currentYear = Calendar.getInstance().get(Calendar.YEAR),
                confirmButtonCLicked = { month, year ->
                    val formattedDate = "${month}/${year}"
                    if (selectedDateField == "start") {
                        startDate = formattedDate
                    } else {
                        endDate = formattedDate
                    }
                    visible = false
                },
                cancelClicked = { visible = false }
            )
        }
    }
}


