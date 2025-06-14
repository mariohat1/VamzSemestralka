package com.example.flashcards.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.ui.theme.LightSkyBlue
import com.example.flashcards.viewModel.PlayViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun PlayScreen(
    viewModel: PlayViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(
        "PlayScreen",
        "Recomposed"
    )
    val playState by viewModel.playState.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val justMarkedKnown by viewModel.justMarked.collectAsState()
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
            modifier = modifier,
            currentIndex = currentIndex,
            isFlipped = isFlipped,
            coroutineScope = coroutineScope,
            justMarkedKnown = justMarkedKnown,
        )
    }
}

@Composable
fun PlayScreenBody(
    innerPadding: PaddingValues,
    flashcards: List<Flashcard>,
    modifier: Modifier = Modifier,
    viewModel: PlayViewModel,
    currentIndex: Int,
    currentFlashcard: Flashcard? = flashcards.getOrNull(currentIndex),
    isFlipped: Boolean,
    coroutineScope: CoroutineScope,
    justMarkedKnown: Boolean,
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (flashcards.isNotEmpty()) {
            val progress = (currentIndex + 1).toFloat() / flashcards.size
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 16.dp),
                color = LightSkyBlue,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                text = "${currentIndex + 1} / ${flashcards.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        currentFlashcard?.let { card ->
            FlashcardCard(
                currentFlashcard = card,
                isFlipped = isFlipped,
                onFlip = { viewModel.flipCard() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            PlayButtons(
                modifier = modifier,
                currentIndex = currentIndex,
                viewModel = viewModel,
                coroutineScope = coroutineScope,
                card = card,
                flashcards = flashcards,
                justMarkedKnown = justMarkedKnown
            )

        } ?: Text(stringResource(R.string.no_flashcards))

    }
}

@Composable
fun PlayButtons(
    modifier: Modifier,
    currentIndex: Int,
    viewModel: PlayViewModel,
    coroutineScope: CoroutineScope,
    card: Flashcard,
    flashcards: List<Flashcard>,
    justMarkedKnown: Boolean
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val cardWidth = if (isPortrait) 0.75f else 0.4f
        Log.d(
            "PlayScreen",
            "Tablet Portrait: $isPortrait, maxWidth: $maxWidth, maxHeight:$maxHeight "
        )
        Row(
            modifier = modifier
                .fillMaxWidth(cardWidth)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Box(
                modifier = modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (currentIndex > 0) {
                    IconButton(
                        onClick = {
                            viewModel.goToPrevCard()
                        },
                        modifier = modifier
                            .clip(CircleShape)
                            .background(LightSkyBlue)

                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.Black
                        )

                    }
                }
            }

            Box(
                modifier = modifier.weight(1.2f),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateFlashcardStatus(card.flashcardId, true)

                        }
                        viewModel.recalculateIndex()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightSkyBlue,
                        contentColor = Color.Black,
                    ),


                    ) {
                    Text(
                        text = stringResource(R.string.known),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Box(
                modifier = modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (currentIndex < flashcards.lastIndex && !justMarkedKnown) {
                    IconButton(
                        onClick = {
                            viewModel.goToNextCard()
                        },
                        modifier = modifier
                            .clip(CircleShape)
                            .background(LightSkyBlue)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.forward),
                            tint = Color.Black
                        )


                    }
                }
            }
        }
    }
}


@Composable
fun FlashcardCard(
    currentFlashcard: Flashcard?,
    isFlipped: Boolean,
    onFlip: () -> Unit,
) {
    currentFlashcard?.let { card ->
        val rotation by animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(350)
        )
        BoxWithConstraints {
            val isPortrait = maxHeight > maxWidth
            val cardWidth = if (isPortrait) 0.75f else 0.4f
            val cardHeight = if (isPortrait) 0.4f else 0.75f

            Card(
                modifier = Modifier
                    .fillMaxWidth(cardWidth)
                    .fillMaxHeight(cardHeight)
                    .graphicsLayer {
                        rotationX = rotation
                        cameraDistance = 12f * density

                    }
                    .clickable(onClick = onFlip),
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
                        Text(
                            text = card.answer,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .graphicsLayer {
                                    rotationX = 180f
                                }
                        )
                    }
                }
            }
        }


    }
}




