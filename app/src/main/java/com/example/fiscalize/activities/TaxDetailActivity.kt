package com.example.fiscalize.activities;

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.example.fiscalize.viewModel.SimplesViewModel;
import kotlin.math.log

@Composable
fun taxDetailActivity(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: SimplesViewModel
) {


    val tax = viewModel.selectedTax

    Log.d("nando magro", "$tax")

    if(tax != null){
        Text(text = "cacete")
    }
}

