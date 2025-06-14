package com.example.flashcards.viewModel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.EditDeckState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EditDeckViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: Int = checkNotNull(savedStateHandle["deckId"])


    val editDeckState: StateFlow<EditDeckState> =
        deckRepository.getDeck(deckId)
            .filterNotNull()
            .map { deck -> EditDeckState(deck, false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = EditDeckState()
            )

    suspend fun deleteFlashcard(flashcard: Flashcard) {
        flashcardRepository.deleteFlashcard(flashcard)
    }

    suspend fun updateFlashcardStatus(id: Int, isKnown: Boolean) {
        flashcardRepository.updateIsKnown(id, isKnown)

    }

    suspend fun updateDeckName(name: String) {
        deckRepository.updateDeck(Deck(deckId, name))
    }


}

