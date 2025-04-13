package com.example.flashcards.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.PlayState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlayViewmodel(
    deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    stateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: Int = checkNotNull(stateHandle["deckId"])
    val playState: StateFlow<PlayState> = flashcardRepository.getUknownFlashcardsByDeckId(deckId)
        .filterNotNull()
        .combine(deckRepository.getDeck(deckId).filterNotNull()) { flashcards, deck ->
            PlayState(
                flashcards = flashcards,
                deckTitle = deck.deck.name,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PlayState()
        )

    suspend fun updateFlaschardStatus(id: Int, isKnown: Boolean) {
        flashcardRepository.updateIsKnown(id, isKnown)

    }
}