package com.example.fiscalize.activities

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fiscalize.model.documents.SimplesCard
import com.example.fiscalize.viewModel.SimplesViewModel


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HistoryActivity(modifier: Modifier = Modifier, navController: NavHostController) {

    val simplesViewModel: SimplesViewModel = viewModel()
    val simplesDocument = simplesViewModel.simplesNacional


    LaunchedEffect(Unit) {
        simplesViewModel.getDocuments()
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 25.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Documento",
                    style = MaterialTheme.typography.h3
                )
            }
        }
        items(simplesDocument) { doc ->
            SimplesCard(doc)
        }
    }
}