package com.example.fiscalize.activities

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
import com.example.fiscalize.viewModel.SimplesViewModel


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HistoryActivity(modifier: Modifier = Modifier, navController: NavHostController, mainHost: NavController, simplesViewModel: SimplesViewModel) {

    val simplesDocument = simplesViewModel.simplesNacional
    val context:Context = LocalContext.current

    LaunchedEffect(Unit) {
        simplesViewModel.getDocuments(context)
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
                    text = "Histórico de documentos",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        items(simplesDocument) { doc ->
            SimplesCard(doc, navController, simplesViewModel, mainHost)
        }
    }
}