package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.database.DeckDao
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.HomeScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    suspend fun insert(deckName: String) {
        deckRepository.insertDeck(Deck(name = deckName))
    }

    val homeScreenState: StateFlow<HomeScreenState> = deckRepository.getDeckWithFlashcards()
        .map { it -> HomeScreenState(it, false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HomeScreenState()
        )

    suspend fun deleteDeck(deck: Deck) {
        flashcardRepository.deleteFlashcardsByDeckId(deck.deckId)
        deckRepository.deleteDeck(deck)

    }


}
