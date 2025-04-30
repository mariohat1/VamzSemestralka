package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.HomeScreenState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    suspend fun insert(deckName: String) {

        deckRepository.insertDeck(Deck(name = deckName))
    }

    val homeScreenState: StateFlow<HomeScreenState> = deckRepository.getDeckWithFlashcards()
        .map { deck -> HomeScreenState(decks = deck, isLoading = false) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeScreenState()
        )


    suspend fun deleteDeck(deck: Deck) {

        flashcardRepository.deleteFlashcardsByDeckId(deck.deckId)
        deckRepository.deleteDeck(deck)

    }


}
