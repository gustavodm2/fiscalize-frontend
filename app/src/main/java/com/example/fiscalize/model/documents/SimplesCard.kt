package com.example.fiscalize.model.documents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fiscalize.viewModel.SimplesViewModel

@Composable
fun SimplesCard(document: SimplesModel, navController: NavController, simplesViewModel: SimplesViewModel, mainHost: NavController) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(64.dp)
            .clickable {
                simplesViewModel.updateSelectedTvShow(document)
                mainHost.navigate("docDetail")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${document.calculationPeriod}\n${document.documentNumber}",
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Ir para detalhes do documento",
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
            )
        }
    }
}