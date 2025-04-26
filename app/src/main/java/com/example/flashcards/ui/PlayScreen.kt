package com.example.flashcards.ui

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.ui.theme.LightSkyBlue
import com.example.flashcards.viewModel.PlayViewmodel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object PlayDestination : NavigationDestination {
    override val route = "play/{deckId}"
    override val titleRes = R.string.app_name
}


@Composable
fun PlayScreen(
    viewModel: PlayViewmodel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playState by viewModel.playState.collectAsState()
    Log.d("PlayScreen", "Recomposed")
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
        if (!playState.isLoading) {
            PlayScreenBody(
                innerPadding,
                flashcards = playState.flashcards,
                viewModel = viewModel,
            )
        }

    }

}


@Composable
fun PlayScreenBody(
    innerPadding: PaddingValues,
    flashcards: List<Flashcard>,
    modifier: Modifier = Modifier,
    viewModel: PlayViewmodel
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val currentFlashcard = flashcards.getOrNull(currentIndex)
    var justMarkedKnown by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentIndex) {
        isFlipped = false
        justMarkedKnown = false
    }



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
                onFlip = { isFlipped = !isFlipped }
            )
            Spacer(modifier = Modifier.height(16.dp))


            BoxWithConstraints(
                modifier = modifier.fillMaxWidth()
            ) {
                val configuration = LocalConfiguration.current
                val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                val cardWidth = if (isPortrait) 0.75f else 0.4f
                Log.d("PlayScreen", "Tablet Portrait: $isPortrait, maxWidth: $maxWidth, maxHeight:$maxHeight ")
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
                                    currentIndex--
                                },
                                modifier = modifier
                                    .clip(CircleShape)
                                    .background(LightSkyBlue)

                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )

                            }
                        }
                    }

                    Box(
                        modifier = modifier.weight(1.1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.updateFlaschardStatus(card.flashcardId, true)
                                    if (currentIndex == flashcards.lastIndex && flashcards.size > 1) {
                                        currentIndex--
                                        justMarkedKnown = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightSkyBlue,
                                contentColor = Color.Black,
                            ),


                            ) {
                            Text(
                                text = "Known",
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
                                    currentIndex++
                                },
                                modifier = modifier
                                    .clip(CircleShape)
                                    .background(LightSkyBlue)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )


                            }
                        }
                    }
                }
            }

        }?: Text("No flashcards available.")

    }
}



@Composable
fun FlashcardCard(
    currentFlashcard: Flashcard?,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier,
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




