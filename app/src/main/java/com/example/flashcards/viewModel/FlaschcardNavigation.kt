package com.example.flashcards.viewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.data.database.FlaschcardDatabase
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.HomeScreenState
import com.example.flashcards.ui.EditNavigation
import com.example.flashcards.ui.FlashcardEditScreen
import com.example.flashcards.ui.HomeDestination
import com.example.flashcards.ui.HomeScreen
import com.example.flashcards.ui.PlayDestination
import com.example.flashcards.ui.PlayScreen
import com.example.flashcards.ui.UpdateDestination
import com.example.flashcards.ui.UpdateScreen


@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    deckRepository: DeckRepository,
    flashcardRepository: FlashcardRepository,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current.applicationContext


    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {

            HomeScreen(
                HomeScreenViewModel(
                    deckRepository,
                    flashcardRepository = flashcardRepository
                ),
                modifier = Modifier,
                navigateToUpdateScreen = { deckId ->
                    navController.navigate("update/$deckId")
                },
                navigateToPlayScreen = { deckId ->
                    navController.navigate("play/$deckId")
                }
            )
        }
        composable(
            route = UpdateDestination.route, // Parametrizovaná routa s deckId
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            Log.d("UpdateScreen", "Deck ID: $deckId")
            UpdateScreen(
                viewModel = UpdateDeckViewModel(
                    deckRepository = deckRepository,
                    flashcardRepository = flashcardRepository,
                    savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId))
                ),
                modifier = Modifier,
                navigateToEditScreen = { flashcardId ->
                    navController.navigate("edit/$deckId/$flashcardId")
                },
                navigateBack = { navController.popBackStack() },
                navigateToEditExistingEditScreen = { flashcardId ->
                    navController.navigate("edit/$deckId/$flashcardId")
                }
            )
        }
        composable(
            route = EditNavigation.route,
            arguments = listOf(
                navArgument("deckId") { type = NavType.IntType },
                navArgument("flashcardId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            val flashcardId = backStackEntry.arguments?.getInt("flashcardId") ?: return@composable
            FlashcardEditScreen(
                viewModel = FlashcardEditViewModel(
                    flashcardRepository = flashcardRepository,
                    deckRepository = deckRepository,
                    savedStateHandle = SavedStateHandle(
                        mapOf("deckId" to deckId, "flashcardId" to flashcardId)
                    )
                ),
                modifier = Modifier,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = PlayDestination.route,
            arguments = listOf(
                navArgument("deckId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            PlayScreen(
                viewModel = PlayViewmodel(
                    deckRepository = deckRepository,
                    stateHandle = SavedStateHandle(mapOf("deckId" to deckId)),
                    flashcardRepository = flashcardRepository
                ),
                navigateBack = {navController.popBackStack()},
                modifier = modifier
            )

        }

    }
}
