package com.example.fiscalize.activities

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fiscalize.components.TopBarComponent
import com.example.fiscalize.viewModel.GraphViewModel

import androidx.compose.material3.Scaffold

@SuppressLint("DefaultLocale")
@Composable
fun TaxDetailActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: GraphViewModel
) {
    val tax = viewModel.selectedTax
    val taxList = tax?.let { viewModel.findAllTaxesByCode(it.code) }

    Scaffold(
        topBar = { TopBarComponent() }
    ) { paddingValues ->
        if (tax != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Detalhes do Imposto",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailTaxItem(label = "Denominação", value = tax.denomination)
                        DetailTaxItem(label = "Código", value = tax.code)
                        DetailTaxItem(label = "Total", value = "R$ ${String.format("%.2f", tax.total)}")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lista de Impostos Relacionados",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                )

                taxList?.forEach { relatedTax ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            DetailTaxItem(label = "Denominação", value = relatedTax.denomination)
                            DetailTaxItem(label = "Código", value = relatedTax.code)
                            DetailTaxItem(label = "Valor", value = "R$ ${String.format("%.2f", relatedTax.total.toFloat())}")
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum imposto disponível.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}


@Composable
fun DetailTaxItem(label: String, value: String?) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}
