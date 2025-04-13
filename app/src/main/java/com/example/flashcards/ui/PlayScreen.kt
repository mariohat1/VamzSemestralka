package com.example.flashcards.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.PlayViewmodel
import kotlinx.coroutines.launch

object PlayDestination : NavigationDestination {
    override val route = "play/{deckId}"
    override val titleRes = R.string.app_name
}


@Composable
fun PlayScreen(
    viewModel: PlayViewmodel, navigateBack: () -> Unit, modifier: Modifier = Modifier
) {
    val playState by viewModel.playState.collectAsState()
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = playState.deckTitle,
                canNavigateBack = true,
                modifier = modifier,
                navigateBack = navigateBack,
            )


        },
    ) { innerPadding ->
        PlayScreenBody(
            innerPadding,
            flashcards = playState.flashcards,
            viewModel = viewModel,
        )
    }

}


@Composable
fun PlayScreenBody(
    innerPadding: PaddingValues,
    flashcards: List<Flashcard>,
    modifier: Modifier = Modifier,
    viewModel: PlayViewmodel
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var currentFlashcard = flashcards.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentIndex >= flashcards.size && flashcards.isNotEmpty()) {
            currentIndex = flashcards.size - 1
        }
        Log.d("PlayScreenBody", "Current Index: $currentIndex")
        currentFlashcard?.let { card ->
            FlashcardCard(
                currentFlashcard = card,
                isFlipped = isFlipped,
                onFlip = { isFlipped = !isFlipped }
            )

            Spacer(modifier = Modifier.height(32.dp))
            val isKnown = card.isKnown

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateFlaschardStatus(
                                id = card.flashcardId,
                                isKnown = !card.isKnown
                            )
                        }
                    }) {
                    Text(
                        if (isKnown) "Mark as Unknown"
                        else "Mark as Known"
                    )
                }
            }


            if (currentIndex > 0) {
                Button(
                    onClick = {
                        currentIndex--
                        isFlipped = false
                    }) {
                    Text("Previous")
                }
            }

            if (currentIndex < flashcards.size - 1) {
                Button(
                    onClick = {
                        currentIndex++
                        isFlipped = false
                    }) {
                    Text("Next")
                }
            }


        } ?: run {
            Text("No flashcards available.")
        }
    }
}


@Composable
fun FlashcardCard(
    currentFlashcard: Flashcard?,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    currentFlashcard?.let { card ->
        val rotation by animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(500)
        )
        Card(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .graphicsLayer {
                    rotationX = rotation
                    cameraDistance = 6f * density
                }
                .clickable(onClick = onFlip),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                if (rotation < 90f) {
                    Text(
                        text = card.question,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Text(
                        text = card.answer,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .graphicsLayer {
                                rotationX = 180f
                            })
                }
            }
        }
    }
}




