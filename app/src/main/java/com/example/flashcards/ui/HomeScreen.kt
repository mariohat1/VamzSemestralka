package com.example.flashcards.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcards.data.states.HomeScreenState
import com.example.flashcards.viewModel.HomeScreenViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.flashcards.data.database.Database
import com.example.flashcards.data.entities.DeckWithFlashcards


@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState
    )
{

    if (homeScreenState.decks.isEmpty()) {

        Text(text = "No decks available", style = MaterialTheme.typography.bodyLarge)
    }else
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(homeScreenState.decks.size) { deck ->
            Text(text = "Item:$deck")

        }


    }

}



@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeScreenState())

}



