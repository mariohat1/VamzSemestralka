package com.example.flashcards.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.viewModel.FlashcardEditViewModel
import kotlinx.coroutines.launch

object EditNavigation : NavigationDestination {
    override var route = "edit/{deckId}/{flashcardId}"
    override val titleRes = R.string.app_name
}

@Composable
fun FlashcardEditScreen(
    viewModel: FlashcardEditViewModel,
    modifier: Modifier,
    navigateBack: () -> Unit
) {

    // Collect state using collectAsState
    val flashcardEditState by viewModel.flashcardScreenState.collectAsState()

    // Handle nullable flashcard state
    val flashcard = flashcardEditState.flashcard

    // Use rememberSaveable only if the flashcard data is available
    var questionText by rememberSaveable { mutableStateOf(flashcard.question) }
    var answerText by rememberSaveable { mutableStateOf(flashcard.answer) }

    // Once data is available, update the state values
    LaunchedEffect(flashcard) {
        flashcard.let {
            questionText = it.question
            answerText = it.answer
        }
    }
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = flashcardEditState.deckTitle,
                canNavigateBack = true,
                modifier = modifier,
                navigateBack = navigateBack,
            )


        },
    ) { innerPadding ->
        EditBody(
            innerPadding,
            viewModel,
            question = questionText,
            onQuestionChange = { questionText = it },
            answer = answerText,
            onAnswerChange = { answerText = it },
            OnSave = navigateBack,
        )

    }
}


@Composable
private fun EditBody(
    innerPadding: PaddingValues,
    viewModel: FlashcardEditViewModel,
    question: String,
    onQuestionChange: (String) -> Unit,
    answer: String,
    onAnswerChange: (String) -> Unit,
    OnSave: () -> Unit 

    ) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(innerPadding)) {
        TextField(
            value = question,
            onValueChange = onQuestionChange,
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = answer,
            onValueChange = onAnswerChange,
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                coroutineScope.launch {
                    viewModel.saveFlashcard(question = question, answer = answer)
                }
                OnSave()

            },
        ) {
            Text("Save")
        }
    }
}
