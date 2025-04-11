package com.example.flashcards.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.flashcards.NavigationDestination
import com.example.flashcards.R
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.viewModel.FlashcardEditViewModel

object EditNavigation : NavigationDestination {
    override val route = "edit/{deckId}/{flashcardId}"
    override val titleRes = R.string.app_name
}

@Composable
fun FlashcardEditScreen(
    viewModel: FlashcardEditViewModel
) {

    val flashcardEditState by viewModel.flashcardScreenState.collectAsState()

    // UI pre editovanie flashcard
    Column(modifier = Modifier.padding(16.dp)) {
        // TextField pre otázku
        TextField(
            value = flashcardEditState.flashcard.question,
            onValueChange = { newQuestion ->
                // Tu môžeš pridať logiku pre aktualizáciu otázky v ViewModel
                // Napríklad zavolať viewModel.updateQuestion(newQuestion)
            },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField pre odpoveď
        TextField(
            value = flashcardEditState.flashcard.answer,
            onValueChange = { newAnswer ->
                // Tu môžeš pridať logiku pre aktualizáciu odpovede v ViewModel
                // Napríklad zavolať viewModel.updateAnswer(newAnswer)
            },
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tlačidlo na uloženie
        Button(
            onClick = {
                // Tu pridaj logiku na uloženie flashcard
                // Napríklad zavolať viewModel.saveFlashcard()
            },
        ) {
            Text("Save")
        }
    }
}