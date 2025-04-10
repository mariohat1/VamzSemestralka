package com.example.flashcards

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.FlashcardNavHost

@Composable
fun FlashcardApp(navController: NavHostController = rememberNavController()) {
    FlashcardNavHost(navController = navController)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,


    ) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, // Hlavná farba pozadia
            titleContentColor = MaterialTheme.colorScheme.onPrimary, // Farba textu
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Farba ikony navigácie
        )
    )

}