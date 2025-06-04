package com.example.flashcards

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.data.database.FlaschcardDatabase
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.ui.theme.LightSkyBlue
import com.example.flashcards.viewModel.FlashcardNavHost

object LengthConstants {
    const val MAX_DECK_NAME_LENGTH = 20
    const val MAX_CARD_QUESTION_LENGTH = 100
}
@Composable
fun FlashcardApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current.applicationContext
    val database = FlaschcardDatabase.getDatabase(context)
    val deckRepository =
        DeckRepository(
            database.deckDao()
        )

    val flashcardRepository = FlashcardRepository(database.flashcardDao())

    FlashcardNavHost(
        navController = navController,
        deckRepository = deckRepository,
        flashcardRepository = flashcardRepository
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {


    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                    )
                }
            }
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF42A5F5),
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun FlashcardBottomBar(
    onClick: () -> Unit,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,


        ) {
        Spacer(modifier = Modifier.weight(1f))

        FloatingActionButton(
            modifier = Modifier.padding(8.dp),
            onClick = onClick,
            containerColor = LightSkyBlue,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()

        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add Deck"
            )
        }
    }
}



