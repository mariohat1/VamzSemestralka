package com.example.flashcards.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.database.DeckDao
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.states.HomeScreenState
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
    val homeScreenState: StateFlow<HomeScreenState> = flow{ deckRepository.getAllDecks().collect() {
        emit(HomeScreenState(it))
    } }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeScreenState()
        )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}
