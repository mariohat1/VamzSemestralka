package com.example.flashcards.ui

import android.content.ClipData.Item
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.flashcards.HomeScreenTopBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Deck

import com.example.flashcards.viewModel.HomeScreenViewModel

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier,
    navController: NavController
) {
    val homeScreenState by viewModel.homeScreenState.collectAsState()

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
            //TODO
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Deck")
            }
        },
        ) { innerPadding ->
        HomeBody(
            decks = homeScreenState.decks,
            modifier = Modifier,
            onItemClick = {},
            contentPadding = innerPadding
        )

    }

}



@Composable
fun HomeBody(
    decks: List<Deck>,
    modifier: Modifier,
    onItemClick: (Deck) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
            )        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),

                modifier = modifier.fillMaxSize(), contentPadding = contentPadding
            ) {
                items(items = decks, key = { it.deckId }) { item ->
                    DeckItem(
                        item = item,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(item) })


                }
            }

        }
    }
}

@Preview
@Composable
fun HomeBodyPreview() {
    HomeBody(
        decks = listOf(
            Deck(1, "Nieco"), Deck(
                2, "Biologia"
            ), Deck(
                3, "Nemcina"
            ), Deck(4, "ASDASDADADSDSADS")

        ), modifier = Modifier, onItemClick = {})


}

@Preview(showBackground = true)
@Composable
fun DeckItemPreview() {
    DeckItem(Deck(1, "Nieco"), modifier = Modifier)


}

@Composable
fun DeckItem(
    item: Deck, modifier: Modifier
) {
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = item.name,

                style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
            )


        }
    }
}







