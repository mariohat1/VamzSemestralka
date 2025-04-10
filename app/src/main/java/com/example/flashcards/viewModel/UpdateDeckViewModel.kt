package com.example.flashcards.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.states.HomeScreenState
import com.example.flashcards.data.states.UpdateDeckState
import com.example.flashcards.viewModel.HomeScreenViewModel.Companion.TIMEOUT_MILLIS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UpdateDeckViewModel(private val deckRepository: DeckRepository) : ViewModel() {

    val updateDeckState: StateFlow<HomeScreenState> = deckRepository.getDecksWithFlashcards(deckId = id)
        .map { decks -> HomeScreenState(decks) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            HomeScreenState()
        )
    }
}