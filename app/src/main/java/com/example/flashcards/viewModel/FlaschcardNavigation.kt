package com.example.flashcards.viewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.ui.FlashcardEditScreen
import com.example.flashcards.ui.HomeScreen
import com.example.flashcards.ui.PlayScreen
import com.example.flashcards.ui.EditDeckScreen
import com.example.flashcards.viewModel.Routes.ROUTE_EDIT
import com.example.flashcards.viewModel.Routes.ROUTE_HOME
import com.example.flashcards.viewModel.Routes.ROUTE_PLAY
import com.example.flashcards.viewModel.Routes.ROUTE_UPDATE


object Routes {
const val ROUTE_HOME = "home"
const val ROUTE_UPDATE = "update/{deckId}"
const val ROUTE_PLAY = "play/{deckId}"
const val ROUTE_EDIT = "edit/{deckId}/{flashcardId}"
}

@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    deckRepository: DeckRepository,
    flashcardRepository: FlashcardRepository,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME,
        modifier = modifier
    ) {

        composable(route = ROUTE_HOME) {
            HomeScreen(
                viewModel = viewModel( initializer = {HomeScreenViewModel(
                    deckRepository,
                    flashcardRepository)}),
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
            route = ROUTE_UPDATE,
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: return@composable
            val viewmodel = viewModel(initializer = {
                EditDeckViewModel(
                    deckRepository = deckRepository,
                    flashcardRepository = flashcardRepository,
                    savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId))
                ) })
            EditDeckScreen(
                viewModel = viewmodel,
                modifier = Modifier,
                navigateToEditScreen = { flashcardId ->
                    navController.navigate("edit/$deckId/$flashcardId")
                },
                navigateBack = { navController.navigate(ROUTE_HOME) {
                    popUpTo(ROUTE_HOME) { inclusive = true }
                } },

            )
        }
        composable(
            route = ROUTE_EDIT,
            arguments = listOf(
                navArgument("deckId") { type = NavType.IntType },
                navArgument("flashcardId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val deckId = backStackEntry.arguments?.getInt("deckId")
            val flashcardId = backStackEntry.arguments?.getInt("flashcardId")
            val viewmodel = viewModel(initializer = { FlashcardEditViewModel(
                flashcardRepository = flashcardRepository,
                deckRepository = deckRepository,
                savedStateHandle = SavedStateHandle(mapOf("deckId" to deckId, "flashcardId" to flashcardId))
            )})
            FlashcardEditScreen(
                viewModel = viewmodel,
                modifier = Modifier,
                navigateBack = {

                    navController.popBackStack() }
            )
        }
        composable(
            route = ROUTE_PLAY,
            arguments = listOf(
                navArgument("deckId") { type = NavType.IntType },

                )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId")
            val viewmodel = viewModel(initializer = {  PlayViewModel(
                deckRepository,
                flashcardRepository,
                stateHandle = SavedStateHandle(mapOf("deckId" to deckId)),
            )})

            PlayScreen(
                viewModel = viewmodel,
                navigateBack = { navController.navigate(ROUTE_HOME) {
                    popUpTo(ROUTE_HOME) { inclusive = true }}},
                modifier = modifier
            )

        }

    }
}
