package com.example.flashcards.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.R
import com.example.flashcards.viewModel.FlashcardEditViewModel
import kotlinx.coroutines.launch


@Composable
fun FlashcardEditScreen(
    viewModel: FlashcardEditViewModel, modifier: Modifier, navigateBack: () -> Unit
) {
    val flashcardEditState by viewModel.flashcardScreenState.collectAsState()
    val questionText by viewModel.questionText.collectAsState()
    val answerText by viewModel.answerText.collectAsState()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Spacer(modifier = Modifier.weight(1f))
                val isEnabled = questionText.isNotBlank() && answerText.isNotBlank()
                val coroutineScope = rememberCoroutineScope()
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveFlashcard(questionText, answerText)
                        }
                        navigateBack()
                    },
                    enabled = isEnabled
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        },
        topBar = {
            FlashcardTopAppBar(
                title = flashcardEditState.deckTitle,
                canNavigateBack = true,
                modifier = modifier,
                navigateBack = navigateBack,
            )
        }        ,
    ) { innerPadding ->
        EditBody(
            innerPadding,
            question = questionText,
            onQuestionChange = { viewModel.onQuestionChanged(it) },
            answer = answerText,
            onAnswerChange = { viewModel.onAnswerChanged(it) },
        )
    }
}


@Composable
fun EditBody(
    innerPadding: PaddingValues,
    question: String,
    onQuestionChange: (String) -> Unit,
    answer: String,
    onAnswerChange: (String) -> Unit,

) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        TextField(
            value = question,
            onValueChange = onQuestionChange,
            label = { Text(stringResource(R.string.question)) },
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = answer,
            onValueChange = onAnswerChange,
            label = { Text(stringResource(R.string.answer)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            )
        Spacer(modifier = Modifier.height(16.dp))

    }
}
