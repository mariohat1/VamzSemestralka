package com.example.flashcards.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcards.FlashcardBottomBar
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.R
import com.example.flashcards.LengthConstants
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.states.HomeState
import com.example.flashcards.ui.theme.LightBlue
import com.example.flashcards.ui.theme.LightSkyBlue
import com.example.flashcards.viewModel.HomeScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    navigateToUpdateScreen: (Int) -> Unit,
    navigateToPlayScreen: (Int) -> Unit,

    ) {
    Log.d("HomeScreen", "Recomposed")
    val homeScreenState by viewModel.homeState.collectAsState()
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var deckName by rememberSaveable { mutableStateOf("") }
    Scaffold(
        bottomBar = {
            FlashcardBottomBar(
                onClick = { isDialogOpen = true }
            )
        },
        topBar = {
            FlashcardTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                modifier = modifier,
                navigateBack = { },
            )
        },
    ) { contentPadding ->
        if (isDialogOpen) {
            CreateDeckDialog(
                onDismiss = {
                    isDialogOpen = false
                    deckName = ""
                },
                onSave = { name ->
                    coroutineScope.launch {
                        viewModel.insert(name)
                    }
                    isDialogOpen = false
                    deckName = ""
                },
                deckName = deckName,
                onNameChange = {
                    if (it.length <= LengthConstants.MAX_DECK_NAME_LENGTH) deckName = it
                }
            )
        }
        HomeBody(
            decks = homeScreenState.decks,
            modifier = Modifier,
            onItemClick = navigateToPlayScreen,
            contentPadding = contentPadding,
            navigateToUpdateScreen = navigateToUpdateScreen,
            viewModel = viewModel,
            homeState = homeScreenState
        )
    }
}

@Preview
@Composable
fun CreateDeckDialogPreview() {
    CreateDeckDialog(
        onDismiss = { },
        onSave = { },
        deckName = TODO(),
        onNameChange = TODO()
    )
}

@Composable
fun CreateDeckDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    deckName: String,
    onNameChange: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(deckName)
                    onDismiss()
                },
                enabled = deckName.isNotBlank(),
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.create_deck)) },
        text = {
            OutlinedTextField(
                value = deckName,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.name)) },
                singleLine = true,
            )
        }
    )
}


@Composable
fun HomeBody(
    decks: List<DeckWithFlashcards>,
    modifier: Modifier,
    onItemClick: (Int) -> Unit,
    navigateToUpdateScreen: (Int) -> Unit,
    contentPadding: PaddingValues,
    viewModel: HomeScreenViewModel,
    homeState: HomeState

) {
    var deckToDelete by remember { mutableStateOf<Deck?>(null) }
    val coroutineScope = rememberCoroutineScope()
    when {
        homeState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,

                ) {
                CircularProgressIndicator(
                    modifier = Modifier,
                )
            }
        }

        homeState.decks.isEmpty() -> {
            Text(
                text = stringResource(R.string.no_deck_found),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            )
        }

        !homeState.isLoading -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = modifier
                    .fillMaxSize(),

                contentPadding = contentPadding,
                userScrollEnabled = true
            ) {
                items(items = decks, key = { it.deck.deckId }) { item ->
                    DeckItem(
                        item = item,
                        modifier = Modifier
                            .clickable {
                                Log.d(
                                    "Navigation",
                                    "Screen s deckId: ${item.deck.deckId}"
                                )
                                onItemClick(item.deck.deckId)
                            },
                        navigateToUpdateScreen = navigateToUpdateScreen,
                        onRequestDelete = { deckToDelete = it })


                }
            }


        }
    }
    deckToDelete?.let { deck ->
        DeleteDialog(
            onDismiss = { deckToDelete = null },
            onConfirm = {
                coroutineScope.launch(Dispatchers.IO) {
                    viewModel.deleteDeck(deck)
                    deckToDelete = null
                }
            }
        )
    }


}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.confirm_deletion)) },
        text = { Text(stringResource(R.string.delete_deck_question)) },
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


@Preview
@Composable
fun HomeBodyPreview() {
    val decks = listOf(
        DeckWithFlashcards(
            deck = Deck(1, "Nieco"),
            flashcards = listOf(
                Flashcard(1, 1, "Odpoveď 1", "1"),
                Flashcard(2, 1, "Odpoveď 2", "1")
            )
        ),
    )
    HomeBody(
        decks = decks,
        modifier = Modifier,
        onItemClick = {},
        navigateToUpdateScreen = TODO(),
        contentPadding = TODO(),
        viewModel = TODO(),
        homeState = TODO(),
    )
}


@Composable
fun DeckItem(
    item: DeckWithFlashcards,
    modifier: Modifier,
    navigateToUpdateScreen: (Int) -> Unit,
    onRequestDelete: (Deck) -> Unit

) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier
                .background(LightSkyBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = item.deck.name,
                Modifier
                    .background(LightSkyBlue)
                    .fillMaxWidth()
                    .height(55.dp),

                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,

                )
            Text(
                modifier = Modifier
                    .background(LightSkyBlue),
                text = " terms: ${item.flashcards.size}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,

            ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.more_options))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        navigateToUpdateScreen(item.deck.deckId)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        onRequestDelete(item.deck)

                    }
                )
            }
        }
    }
}








