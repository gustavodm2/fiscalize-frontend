package com.example.fiscalize.viewModel

import androidx.lifecycle.ViewModel
import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.model.documents.Tax

class SimplesViewModel : ViewModel() {

    val firstDoc: SimplesModel = SimplesModel(
        1,
        "11.222.333/0001-44",
        "Clovis Pereira",
        maturityDate = "23/09/2024",
        calculationPeriod = "08/2024",
        documentNumber = "11.22.33333.4444444-5",
        observations = listOf(
            Tax("1001", "IRPJ - SIMPLES NACIONAL 08/2024", "717.69","", "", "717.69")
        )
    )

    val secondDoc: SimplesModel = SimplesModel(
        2,
        "11.222.333/0001-44",
        "Clovis Pereira",
        maturityDate = "23/09/2024",
        calculationPeriod = "08/2024",
        documentNumber = "11.22.33333.4444444-5",
        observations = listOf(
            Tax("1001", "IRPJ - SIMPLES NACIONAL 08/2024", "717.69","", "", "717.69")
        )
    )

}