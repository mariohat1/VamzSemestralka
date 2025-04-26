package com.example.flashcards.viewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
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


    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {


        composable(route = HomeDestination.route) {
        val viewmodel = remember {
            HomeScreenViewModel(
                deckRepository = deckRepository,
                flashcardRepository = flashcardRepository
            )
        }
            HomeScreen(
                viewModel = viewmodel,
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
            route = UpdateDestination.route,
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            val viewmodel = remember(deckId) {
                UpdateDeckViewModel(
                    deckRepository = deckRepository,
                    flashcardRepository = flashcardRepository,
                    savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId))
                )
            }
            Log.d("Navigation", "Vykonavam composable pre UpdateeDeck s deckId: ${backStackEntry.arguments?.getInt("deckId")}")
            UpdateScreen(
                viewModel = viewmodel,
                modifier = Modifier,
                navigateToEditScreen = { flashcardId ->
                    navController.navigate("edit/$deckId/$flashcardId")
                },
                navigateBack = { navController.navigate(HomeDestination.route) {
                    popUpTo(HomeDestination.route) { inclusive = true }
                } },
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
            val viewmodel = FlashcardEditViewModel(
                flashcardRepository = flashcardRepository,
                deckRepository = deckRepository,
                savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId, "flashcardId" to flashcardId))
            )
            FlashcardEditScreen(
                viewModel = viewmodel,
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
            Log.d("Navigation", "Vykonavam composable pre PlayDestination s deckId: ${backStackEntry.arguments?.getInt("deckId")}")
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            val viewModel = remember(deckId) {
                PlayViewmodel(
                    deckRepository = deckRepository,
                    flashcardRepository = flashcardRepository,
                    stateHandle = SavedStateHandle(mapOf("deckId" to deckId))
                )
            }

            PlayScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack()
                 },
                modifier = modifier
            )

        }

    }
}
