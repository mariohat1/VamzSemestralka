package com.example.flashcards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.UpdateDeckViewModel

object UpdateDestination : NavigationDestination {
    override val route = "update/{deckId}"
    override val titleRes = R.string.app_name
}

@Composable
fun UpdateScreen(
    viewModel: UpdateDeckViewModel,
    navigateToEditScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToEditExistingEditScreen: (Int) -> Unit
    
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val updateScreenState by viewModel.updateDeckState.collectAsState()

    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = updateScreenState.deck.deck.name,
                canNavigateBack = true,
                modifier = modifier,
                navigateBack = navigateBack,
            )


        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isDialogOpen.value = true

            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Deck")
            }
        },

        ) { innerPadding ->

        if (isDialogOpen.value) {
            CreateUpdateConfirmDialog(

                deck = updateScreenState.deck,
                onDismiss = { isDialogOpen.value = false },
                navigateToEditScreen = navigateToEditScreen,
            )
        }
        if (!updateScreenState.isLoading) {
            UpdateBody(
                modifier = modifier,
                paddingValues = innerPadding,
                flashcards = updateScreenState.deck.flashcards,
                navigateToEditExistingEditScreen = navigateToEditExistingEditScreen
            )
        }
    }


}

@Composable
fun UpdateBody(
    paddingValues: PaddingValues,
    modifier: Modifier,
    flashcards: List<Flashcard>,
    navigateToEditExistingEditScreen: (Int) -> Unit
) {
    if (flashcards.isEmpty()) {
        Text(
            text = "prazdnota",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(paddingValues),
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(flashcards) { flashcard ->
                FlashCardItem(
                    flashcard = flashcard,
                    modifier = modifier,
                    navigateToEditExistingEditScreen = navigateToEditExistingEditScreen
                )
            }
        }
    }
}

@Composable
fun CreateUpdateConfirmDialog(
    deck: DeckWithFlashcards,
    onDismiss: () -> Unit,
    navigateToEditScreen: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirm action")
        },
        text = {
            Text("Are you sure you want to create a new flashcard in the deck '${deck.deck.name}'?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navigateToEditScreen(-1)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun FlashCardItemPreview() {
    FlashCardItem(
        flashcard = Flashcard(
            flashcardId = 1,
            deckId = 1,
            question = "What is the capital of France?",
            answer = "Paris"
        ),
        modifier = Modifier,
        navigateToEditExistingEditScreen = TODO()
    )
}


@Composable
fun FlashCardItem(
    flashcard: Flashcard,
    modifier: Modifier,
    navigateToEditExistingEditScreen: (Int) -> Unit,

) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        onClick ={ navigateToEditExistingEditScreen(flashcard.flashcardId)}
    ) {
        Text(
            text = flashcard.question,
            Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showBackground = true)
@Composable
fun UpdateBodyPreview() {
    val sampleFlashcards = listOf(
        Flashcard(1, 1, "What is 2 + 2?", "4"),
        Flashcard(2, 1, "What is the capital of Spain?", "Madrid")
    )

    UpdateBody(
        paddingValues = PaddingValues(16.dp),
        modifier = Modifier,
        flashcards = sampleFlashcards,
        navigateToEditExistingEditScreen = TODO()
    )
}

    



