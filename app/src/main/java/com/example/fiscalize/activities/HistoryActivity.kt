package com.example.fiscalize.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.fiscalize.components.MonthPicker
import com.example.fiscalize.components.TopBarComponent
import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.ui.theme.mainRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    val filteredDocuments by graphViewModel::simplesNacional
    val hasMoreDocuments by graphViewModel::hasMorePages

    var visible by remember { mutableStateOf(false) }
    var selectedDateField by remember { mutableStateOf("") }
    var startDate by rememberSaveable { mutableStateOf("de") }
    var endDate by rememberSaveable { mutableStateOf("até") }

    var currentPage by rememberSaveable { mutableStateOf(1) }

    val listState = rememberLazyListState()

    fun loadDocuments() {
        if (hasMoreDocuments) {
            if (startDate != "de" && endDate != "até") {
                graphViewModel.getDocuments(context, page = currentPage, startDate, endDate)
            } else {
                graphViewModel.getDocumentsWODate(context, page = currentPage)
            }
            currentPage++
        }
    }

    LaunchedEffect(Unit) {
        loadDocuments()
    }


    Scaffold(
        topBar = { TopBarComponent() }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
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
                            .size(32.dp)
                            .clickable {
                                currentPage = 1
                                graphViewModel.resetDocuments()
                                loadDocuments()
                            }
                    )
                }
            }

            items(filteredDocuments) { doc ->
                Log.d("LazyColumnDoc", "Documento: $doc")
                SimplesCard(doc, navController, graphViewModel, mainHost)
            }

            if (hasMoreDocuments) {
                item {
                    Button(
                        onClick = { loadDocuments() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainRed,
                        )

                    ) {
                        Text("Carregar mais documentos", color = Color.White)
                    }
                }
            } else if (filteredDocuments.isNotEmpty()) {
                item {
                    Text(
                        text = "Todos os documentos foram carregados.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
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
                        startDate = graphViewModel.formatDate(formattedDate)
                    } else {
                        endDate = graphViewModel.formatDate(formattedDate)
                    }
                    currentPage = 1
                    graphViewModel.resetDocuments()
                    loadDocuments()
                    visible = false
                },
                cancelClicked = { visible = false },
            )
        }
    }
}


