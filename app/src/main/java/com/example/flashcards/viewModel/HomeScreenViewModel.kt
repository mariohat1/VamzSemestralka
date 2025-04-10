package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.database.DeckDao
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.states.HomeScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val deckRepository: DeckRepository) : ViewModel(){
    suspend fun insert(deckName: String) {
        deckRepository.insertDeck(Deck(name = deckName))
    }

    val homeScreenState: StateFlow<HomeScreenState> = deckRepository.getDecksWithFlashcards()
        .map { decks -> HomeScreenState(decks) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            HomeScreenState()
        )

    fun getDeckWithFlashcards(deckId: Int): Flow<List<DeckWithFlashcards>> {
        return deckRepository.getDeckWithFlashcards(deckId)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}
