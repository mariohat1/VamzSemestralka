package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.HomeState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    suspend fun insert(deckName: String) {
        deckRepository.insertDeck(Deck(name = deckName))
    }

    val homeState: StateFlow<HomeState> = deckRepository.getDeckWithFlashcards()
        .filterNotNull()
        .map { deck -> HomeState(decks = deck, isLoading = false) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState()
        )


    suspend fun deleteDeck(deck: Deck) {
        flashcardRepository.deleteFlashcardsByDeckId(deck.deckId)
        deckRepository.deleteDeck(deck)
    }


}
