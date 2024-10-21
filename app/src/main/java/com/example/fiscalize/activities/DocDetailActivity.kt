package com.example.fiscalize.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fiscalize.model.documents.TaxModel
import com.example.fiscalize.viewModel.SimplesViewModel

@Composable
fun DocDetailActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SimplesViewModel = viewModel()
) {
    val document = viewModel.selectedDocument

    if (document != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Detalhes do Documento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,

            )

            Spacer(modifier = Modifier.height(24.dp))

            DetailItem(label = "CNPJ", value = document.CNPJ)
            DetailItem(label = "Data de Vencimento", value = document.maturityDate)
            DetailItem(label = "Número do Documento", value = document.documentNumber)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Impostos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(document.taxes) { tax ->
                    TaxCard(tax)
                }
            }
        }
    } else {
        Text(
            text = "Nenhum documento disponível.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun DetailItem(label: String, value: String?) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TaxCard(tax: TaxModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tax.denomination,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Código: ${tax.code}")
            Text(text = "Valor Principal: R$ ${tax.principal}")
            if(tax.fine.isNotBlank()){
                Text(text = "Multa: $tax")
            }
            if(tax.fine.isNotBlank()) {
                Text(text = "Taxas: $tax")
            }
            Text(text = "Total: R$ ${tax.total}")
        }
    }
}