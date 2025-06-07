package com.example.flashcards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashcards.FlashcardBottomBar
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.LengthConstants
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.EditDeckViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun EditDeckScreen(
    viewModel: EditDeckViewModel,
    navigateToEditScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {

    val editDeckState by viewModel.editDeckState.collectAsState()
    var deckName by rememberSaveable { mutableStateOf(editDeckState.deck.deck.name) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(editDeckState.deck.deck.name) {
        if (deckName.isEmpty()) {
            deckName = editDeckState.deck.deck.name
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            FlashcardTopAppBar(
                title = editDeckState.deck.deck.name,
                canNavigateBack = true,
                modifier = modifier,
                navigateBack = navigateBack,
            )


        },
        bottomBar = {
            FlashcardBottomBar(
                onClick = { navigateToEditScreen(-1) }
            )
        },
    ) { contentPadding ->
        EditBody(
            modifier = modifier,
            paddingValues = contentPadding,
            flashcards = editDeckState.deck.flashcards,
            navigateToEditExistingEditScreen = navigateToEditScreen,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            deckName = deckName,
            coroutineScope = coroutineScope,
            onDeckNameChange = { deckName = it },
        )
    }
}

@Composable
fun EditBody(
    paddingValues: PaddingValues,
    modifier: Modifier,
    flashcards: List<Flashcard>,
    viewModel: EditDeckViewModel,
    navigateToEditExistingEditScreen: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    deckName: String,
    onDeckNameChange: (String) -> Unit,
    coroutineScope: CoroutineScope,
) {
    var flashcardToDeleteId by rememberSaveable { mutableStateOf<Int?>(null) }
    val flashcardToDelete = flashcards.find { it.flashcardId == flashcardToDeleteId }
    Column(
        modifier = Modifier
            .padding(paddingValues),

        ) {
        OutlinedTextField(
            value = deckName,
            onValueChange = {
                if (it.length <= LengthConstants.MAX_DECK_NAME_LENGTH) onDeckNameChange(
                    it
                )
            },
            label = { Text(stringResource(R.string.deck_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateDeckName(deckName)
                }
            },
            modifier = Modifier.align(Alignment.Start).padding(start = 8.dp),
            enabled = deckName.isNotEmpty()
        ) {
            Text(stringResource(R.string.save))
        }
        if (flashcards.isEmpty()) {
            Text(
                text = stringResource(R.string.no_flashcards),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = true
            ) {
                items(flashcards) { flashcard ->
                    val messageUnknown = stringResource(R.string.marked_as_unknown)
                    val messageKnown = stringResource(R.string.marked_as_known)
                    FlashCardItem(
                        flashcard = flashcard,
                        modifier = modifier,
                        navigateToEditExistingEditScreen = navigateToEditExistingEditScreen,
                        onToggleKnownStatus = {
                            val message =
                                if (it.isKnown) messageUnknown else messageKnown
                            coroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                viewModel.updateFlashcardStatus(it.flashcardId, !it.isKnown)
                                snackbarHostState.showSnackbar(
                                    message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onRequestDelete = { flashcardToDeleteId = it.flashcardId }
                    )
                }
            }
        }
    }
    flashcardToDelete?.let { flashcard ->
        DeleteConfirmationDialog(
            onDismiss = { flashcardToDeleteId = null },
            onConfirm = {
                coroutineScope.launch {
                    viewModel.deleteFlashcard(flashcard)
                    flashcardToDeleteId = null
                }
            }
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.confirm_deletion)) },
        text = { Text(stringResource(R.string.delete_flashcard_question)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
fun FlashCardItem(
    flashcard: Flashcard,
    modifier: Modifier,
    navigateToEditExistingEditScreen: (Int) -> Unit,
    onToggleKnownStatus: (Flashcard) -> Unit,
    onRequestDelete: (Flashcard) -> Unit,
    ) {
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
                    Icon(icon, contentDescription = stringResource(R.string.known_status))
                }
                IconButton(onClick = { onRequestDelete(flashcard) }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_flashcard)
                    )
                }
            }
        }
    }

}


    



