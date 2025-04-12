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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.FlashcardTopAppBar
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.states.EditFlashcardScreen
import com.example.flashcards.viewModel.FlashcardEditViewModel
import kotlinx.coroutines.launch

object EditNavigation : NavigationDestination {
    override val route = "edit/{deckId}/{flashcardId}"
    override val titleRes = R.string.app_name
}

@Composable
fun FlashcardEditScreen(
    viewModel: FlashcardEditViewModel,
    modifier: Modifier,
    navigateBack: () -> Unit
) {

    val flashcardEditState by viewModel.flashcardScreenState.collectAsState()
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
            viewModel
        )

    }
}


@Composable
private fun EditBody(
    innerPadding: PaddingValues,
    viewModel: FlashcardEditViewModel

) {
    val coroutineScope = rememberCoroutineScope()
    var question by remember { mutableStateOf("") }

    var answer by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(innerPadding)) {

        TextField(
            value = question,
            onValueChange = {
                question = it

            },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = answer,
            onValueChange = {
                answer = it

            },
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.saveFlashcard(question, answer)

                }
            },
        ) {
            Text("Save")
        }

    }
}