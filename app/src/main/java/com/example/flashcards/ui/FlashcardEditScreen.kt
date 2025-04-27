package com.example.flashcards.ui

import android.util.Log
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


    val flashcardEditState by viewModel.flashcardScreenState.collectAsState()


    val flashcard = flashcardEditState.flashcard


    var questionText by rememberSaveable {
        mutableStateOf(
            flashcard.question
        )
    }
    Log.d("EditScreen", "flashcard received: $questionText")
    var answerText by rememberSaveable {
        mutableStateOf(
            flashcard.answer
        )

    }
    Log.d("EditScreen", "flashcard received: $answerText")
    var isEdited by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(flashcard) {

        if (!isEdited && !flashcardEditState.isLoading) {
            questionText = flashcard.question
            answerText = flashcard.answer
            isEdited = true
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
            onSave = navigateBack,
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
    onSave: () -> Unit

) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)) {
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
            modifier = Modifier.fillMaxWidth().
            height(150.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (question.isNotBlank() && answer.isNotBlank()) {
                    coroutineScope.launch {
                        viewModel.saveFlashcard(question = question, answer = answer)

                    }
                    onSave()
                }
            },
            enabled = question.isNotBlank() && answer.isNotBlank(),
        ) {
            Text("Save")
        }
    }
}
