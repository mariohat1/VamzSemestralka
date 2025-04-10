package com.example.flashcards.ui

import android.content.ClipData.Item
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.HomeScreenTopBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.FlashcardNavHost

import com.example.flashcards.viewModel.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    navigateToUpdateScreen: () -> Unit,
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    val isDialogOpen = remember { mutableStateOf(false) }
    val courutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HomeScreenTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                modifier = modifier,
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
            CreateDeckDialog(
                onDismiss = { isDialogOpen.value = false },
                onSave = { deckName ->

                    courutineScope.launch {
                        viewModel.insert(deckName)
                    }

                    isDialogOpen.value = false
                }
            )
        }

        HomeBody(
            decks = homeScreenState.decks,
            modifier = Modifier,
            onItemClick = {},
            contentPadding = innerPadding,
            navigateToUpdateScreen = navigateToUpdateScreen
        )

    }

}

@Preview
@Composable
fun CreateDeckDialogPreview() {

    CreateDeckDialog(
        onDismiss = { },
        onSave = { }
    )


}

@Composable
fun CreateDeckDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var deckName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(deckName)
                    onDismiss()
                },
                enabled = deckName.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Create Deck") },
        text = {
            OutlinedTextField(
                value = deckName,
                onValueChange = { deckName = it },
                label = { Text("Name") },
                singleLine = true
            )
        }
    )
}


@Composable
fun HomeBody(
    decks: List<DeckWithFlashcards>,
    modifier: Modifier,
    onItemClick: (Deck) -> Unit,
    navigateToUpdateScreen: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
    ,

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        if (decks.isEmpty()) {
            Text(
                text = "prazdnota",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),

                modifier = modifier.fillMaxSize(), contentPadding = contentPadding
            ) {
                items(items = decks, key = { it.deck.deckId }) { item ->
                    DeckItem(
                        item = item,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(item.deck) },
                        navigateToUpdateScreen = navigateToUpdateScreen

                    )


                }
            }

        }
    }
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
    )
}


@Composable
fun DeckItem(
    item: DeckWithFlashcards,
    modifier: Modifier,
    navigateToUpdateScreen: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = item.deck.name,
                Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = " terms: ${item.flashcards.size}",
            )

        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,

        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Update") },
                    onClick = navigateToUpdateScreen
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { }
                )
            }


        }
    }
}






