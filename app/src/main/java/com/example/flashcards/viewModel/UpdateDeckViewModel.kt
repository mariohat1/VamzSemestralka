package com.example.flashcards.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.UpdateDeckState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UpdateDeckViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])

    val updateDeckState: StateFlow<UpdateDeckState> =
        deckRepository.getDeck(deckId)
            .filterNotNull()
            .map { deck -> UpdateDeckState(deck,false) }

            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = UpdateDeckState()
            )


}

class UpdateDeckViewModelFactory(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val deckId: Int  // Pass deckId here
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateDeckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpdateDeckViewModel(
                deckRepository = deckRepository,
                flashcardRepository = flashcardRepository,
                savedStateHandle = SavedStateHandle().apply {
                    set("deckId", deckId) // Set deckId in SavedStateHandle
                }
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}