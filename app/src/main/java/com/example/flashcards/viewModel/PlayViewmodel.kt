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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class PlayViewmodel(
    deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: Int = checkNotNull(stateHandle["deckId"])
    private val newIndex = stateHandle.get<Int>("currentIndex")
    private val currentIndexF = MutableStateFlow(newIndex ?: 0)
    val currentIndex: StateFlow<Int> = currentIndexF.asStateFlow()



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


    fun goToNextCard(size: Int) {
        if (currentIndexF.value < size - 1) {
            currentIndexF.value++
            stateHandle["currentIndex"] = currentIndexF.value // Save currentIndex
            Log.d("PlayViewModel", "goToNextCard() - currentIndex saved: ${currentIndexF.value}")
        }
    }

    fun goToPreviousCard() {
        if (currentIndexF.value > 0) {
            currentIndexF.value--
            stateHandle["currentIndex"] = currentIndexF.value // Save currentIndex
            Log.d(
                "PlayViewModel",
                "goToPreviousCard() - currentIndex saved: ${currentIndexF.value}"
            )
        }
    }

    fun decreaseIndex() {
        if (currentIndexF.value == playState.value.flashcards.lastIndex && playState.value.flashcards.size > 1) {
            currentIndexF.value--
            stateHandle["currentIndex"] = currentIndexF.value // Save currentIndex
            Log.d("PlayViewModel", "decreaseIndex() - currentIndex saved: ${currentIndexF.value}")
        }
    }


    suspend fun updateFlaschardStatus(id: Int, isKnown: Boolean) {
        flashcardRepository.updateIsKnown(id, isKnown)

    }
}

class PlayViewModelFactory(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val deckId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewmodel(
                deckRepository,
                flashcardRepository,
                SavedStateHandle().apply {
                    set("deckId", deckId)
                }
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}