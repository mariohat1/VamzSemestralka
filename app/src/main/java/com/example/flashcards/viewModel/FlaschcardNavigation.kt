package com.example.flashcards.viewModel

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController

import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.ui.HomeDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcards.data.database.FlaschcardDatabase
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.ui.HomeScreen


@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current.applicationContext
    val deckRepository = DeckRepository(FlaschcardDatabase.getDatabase(context).deckDao())
    val flashCardRepository = FlashcardRepository(FlaschcardDatabase.getDatabase(context).flashcardDao())

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {     composable(route = HomeDestination.route) {
        HomeScreen(
            HomeScreenViewModel(deckRepository),
            modifier = Modifier,
            navigateToUpdateScreen = {}
        )
        }
    }
}

