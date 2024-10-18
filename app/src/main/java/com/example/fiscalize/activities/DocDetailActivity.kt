package com.example.fiscalize.activities

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.fiscalize.viewModel.SimplesViewModel

@Composable
fun DocDetailActivity(modifier: Modifier = Modifier, navController: NavHostController, viewModel: SimplesViewModel){

    val document = viewModel.selectedDocument

    document.value?.let { Text(text = it._id) }


}