package com.example.flashcards.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.PlayState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class PlayViewmodel(
    deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: Int = checkNotNull(stateHandle["deckId"])


    init {
        Log.d("PlayViewModel", "Inicializujem PlayViewModel pre deckId: $deckId")
    }

    private val deckTitleFlow = deckRepository.getDeck(deckId)
        .filterNotNull()
        .map { it.deck.name }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            initialValue = ""
        )

    val playState: StateFlow<PlayState> = flashcardRepository.getUknownFlashcardsByDeckId(deckId)
        .filterNotNull()
        .combine(deckTitleFlow) { flashcards, deckTitle ->
            PlayState(
                flashcards = flashcards,
                deckTitle = deckTitle,
                isLoading = false
            )
        }.distinctUntilChanged()
            .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            initialValue = PlayState()
        )





    suspend fun updateFlaschardStatus(id: Int, isKnown: Boolean) {
        flashcardRepository.updateIsKnown(id, isKnown)

    }
}

