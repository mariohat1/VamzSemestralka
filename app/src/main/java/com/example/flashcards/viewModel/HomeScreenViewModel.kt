package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.database.DeckDao
import com.example.flashcards.data.states.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val daoDeck: DeckDao) : ViewModel(){
    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState :StateFlow<HomeScreenState> =_uiState.asStateFlow()
    init{
        loadDecks();
    }

    private fun loadDecks() {
        viewModelScope.launch {
            daoDeck.getAllDecks().collect { decks ->
                    _uiState.update { it.copy(decks = decks) }
                }
        }
    }
}
