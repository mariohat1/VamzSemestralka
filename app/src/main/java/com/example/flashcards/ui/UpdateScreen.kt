package com.example.flashcards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.UpdateDeckViewModel

object UpdateDestination : NavigationDestination {
    override val route = "update/{deckId}"
    override val titleRes = R.string.app_name
}

@Composable
fun UpdateScren(
    viewModel: UpdateDeckViewModel,

    modifier: Modifier = Modifier
) {
    val updateScreenState by viewModel.updateDeckState.collectAsState()
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = updateScreenState.deck.deck.name,
                canNavigateBack = false,
                modifier = modifier,
            )


        },
    ) { innerPadding ->
        UpdateBody(
            modifier = modifier,
            paddingValues = innerPadding,
            flashcards = updateScreenState.deck.flashcards
        )

    }


}

@Composable
fun UpdateBody(
    paddingValues: PaddingValues,
    modifier: Modifier,
    flashcards: List<Flashcard>
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flashcards) { flashcard ->
            FlashCardItem(flashcard,
                )
        }
    }
}

@Composable
fun FlashCardItem(
    flashcard: Flashcard) {


}
    



