package com.example.flashcards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.UpdateDeckViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

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

    val updateScreenState by viewModel.updateDeckState.collectAsState()
    var deckName by rememberSaveable { mutableStateOf(updateScreenState.deck.deck.name) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(updateScreenState.deck.deck.name) {
        if (deckName.isEmpty()) {
            deckName = updateScreenState.deck.deck.name
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                navigateToEditScreen(-1)

            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Deck")
            }
        },

        ) { contentPadding ->


        UpdateBody(
            modifier = modifier,
            paddingValues = contentPadding,
            flashcards = updateScreenState.deck.flashcards,
            navigateToEditExistingEditScreen = navigateToEditExistingEditScreen,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            deckName = deckName,
            coroutineScope = coroutineScope,
            onDeckNameChange = {deckName = it},
        )

    }


}

@Composable
fun UpdateBody(
    paddingValues: PaddingValues,
    modifier: Modifier,
    flashcards: List<Flashcard>,
    viewModel: UpdateDeckViewModel,
    navigateToEditExistingEditScreen: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    deckName: String,
    onDeckNameChange: (String) -> Unit,
    coroutineScope: CoroutineScope,
) {

    Column(
        modifier = Modifier
            .padding(paddingValues),

    ) {
        OutlinedTextField(
            value = deckName,
            onValueChange = { onDeckNameChange(it)},
            label = { Text("Deck Name") },
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_large))
        )

        TextButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateDeckName(deckName)
                }
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Save")
        }

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
                .padding(dimensionResource(R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            userScrollEnabled = true
        ) {
            items(flashcards) { flashcard ->
                FlashCardItem(
                    flashcard = flashcard,
                    modifier = modifier,
                    navigateToEditExistingEditScreen = navigateToEditExistingEditScreen,
                    onToggleKnownStatus = {
                        coroutineScope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            viewModel.updateFlashcardStatus(it.flashcardId, !it.isKnown)
                            val message = if (it.isKnown) "Marked as Unknown" else "Marked as Known"
                            snackbarHostState.showSnackbar(
                                message,
                                duration = SnackbarDuration.Short
                            )
                        }


                    },
                    onDeleteFlashcard = {
                        coroutineScope.launch {
                            viewModel.deleteFlashcard(it)
                        }
                    }

                )
            }
        }
    } }
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this flashcard?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
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
        navigateToEditExistingEditScreen = TODO(),
        onToggleKnownStatus = TODO(),
        onDeleteFlashcard = TODO()
    )
}


@Composable
fun FlashCardItem(
    flashcard: Flashcard,
    modifier: Modifier,
    navigateToEditExistingEditScreen: (Int) -> Unit,
    onToggleKnownStatus: (Flashcard) -> Unit,
    onDeleteFlashcard: (Flashcard) -> Unit,


    ) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = { onDeleteFlashcard(flashcard) }
        )

    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxWidth(),

        onClick = { navigateToEditExistingEditScreen(flashcard.flashcardId) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = flashcard.question,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val icon = if (flashcard.isKnown) Icons.Filled.CheckCircle else Icons.Filled.Close
                IconButton(onClick = {
                    onToggleKnownStatus(flashcard)
                }) {
                    Icon(icon, contentDescription = "Toggle Known Status")


                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Flashcard")
                }
            }
        }
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
        navigateToEditExistingEditScreen = TODO(),
        viewModel = TODO(),
        snackbarHostState = TODO(),
        deckName = TODO(),
        onDeckNameChange = TODO(),
        coroutineScope = TODO()
    )
}

    



