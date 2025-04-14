package com.example.flashcards.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val currentFlashcard = flashcards.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Log.d("PlayScreenBody", "Current Index: $currentIndex")
        currentFlashcard?.let { card ->

            FlashcardCard(
                currentFlashcard = card,
                isFlipped = isFlipped,
                onFlip = { viewModel.flip() }
            )

            Spacer(modifier = Modifier.height(32.dp))


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Center the Known button
            ) {
                // "Known" button, always centered
                Button(
                    onClick = {
                        viewModel.decreaseIndex()
                        coroutineScope.launch {
                            viewModel.updateFlaschardStatus(card.flashcardId, true)
                        }

                    },
                ) {
                    Text(text = "Known", maxLines = 1, softWrap = false)
                }



                if (currentIndex > 0) {
                    Button(
                        onClick = { viewModel.goToPreviousCard() },
                        modifier = Modifier
                            .absoluteOffset(x = (-120).dp)
                    ) {
                        Text("Previous")
                    }
                }

                // "Next" button (conditionally displayed), positioned to the right
                if (currentIndex < flashcards.size - 1) {
                    Button(
                        onClick = { viewModel.goToNextCard(flashcards.size) },
                        modifier = Modifier
                            .absoluteOffset(x = 120.dp) // Adjust as needed
                    ) {
                        Text("Next")
                    }
                }
            }
        } ?: Text("No flashcards available.")
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
        BoxWithConstraints(modifier = modifier) {
            val isPortrait = maxHeight > maxWidth
            val cardWidth = if (isPortrait)  0.7f else  0.35f
            val cardHeight = if (isPortrait) 0.35f else 0.7f

            Card(
                modifier = modifier
                    .fillMaxWidth(cardWidth)
                    .fillMaxHeight(cardHeight)
                    .graphicsLayer {
                        rotationX = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable(onClick = onFlip),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation < 90f) {
                        Text(
                            text = card.question,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationX = 180f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = card.answer,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




