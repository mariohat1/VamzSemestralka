package com.example.flashcards.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.data.database.FlaschcardDatabase
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.ui.HomeDestination
import com.example.flashcards.ui.HomeScreen
import com.example.flashcards.ui.UpdateDestination
import com.example.flashcards.ui.UpdateScren


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
    ) {
        composable(route = HomeDestination.route) {
        HomeScreen(
            HomeScreenViewModel(deckRepository),
            modifier = Modifier,
            navigateToUpdateScreen = { deckId ->
                navController.navigate("update/$deckId")
            }
        )
        }
        composable(
            route =  UpdateDestination.route, // Parametrizovaná routa s deckId
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Získanie deckId z argumentov
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            UpdateScren(
                viewModel = UpdateDeckViewModel(
                    deckRepository = deckRepository,
                    flashcardRepository = flashCardRepository,
                    savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId))
                ),
                modifier = Modifier
            )
        }
    }
}

