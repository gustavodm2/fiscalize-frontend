package com.example.fiscalize.model.documents

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

@Composable
fun SimplesCard(document: SimplesModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(64.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(){
            Text(
                document.calculationPeriod,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}