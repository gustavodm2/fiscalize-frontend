package com.example.fiscalize.model.documents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fiscalize.viewModel.SimplesViewModel

@Composable
fun SimplesCard(document: SimplesModel, navController: NavController, simplesViewModel: SimplesViewModel, mainHost: NavController) {
    //navControler => host da tabNavigation
    //mainHost => host default

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
        Row{
            Text(
                document.calculationPeriod,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}