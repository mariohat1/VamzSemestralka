package com.example.flashcards.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.PlayState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlayViewModel(
    deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    val stateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: Int = checkNotNull(stateHandle["deckId"])
    var currentIndex = MutableStateFlow(0)
        private set

    var justMarked = MutableStateFlow(false)
        private set
    var isFlipped = MutableStateFlow(false)
        private set

    private val deckTitleFlow = deckRepository.getDeck(deckId)
        .filterNotNull()
        .map { it.deck.name }
        .distinctUntilChanged()



    val playState: StateFlow<PlayState> = flashcardRepository.getUknownFlashcardsByDeckId(deckId)
        .filterNotNull()
        .combine(deckTitleFlow) { flashcards, deckTitle ->
            PlayState(
                flashcards = flashcards,
                deckTitle = deckTitle,
                isLoading = false
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            initialValue = PlayState()
        )

    fun goToNextCard() {
        currentIndex.value++
        justMarked.value = false
        isFlipped.value = false
    }

    fun goToPrevCard() {
        currentIndex.value--
        justMarked.value = false
        isFlipped.value = false
    }

    fun recalculateIndex() {
        if (currentIndex.value == playState.value.flashcards.lastIndex && playState.value.flashcards.size > 1) {
            currentIndex.value--
            justMarked.value = true
        }
    }
    fun flipCard() {
        isFlipped.value = !isFlipped.value
    }

    suspend fun updateFlashcardStatus(id: Int, isKnown: Boolean) {
        flashcardRepository.updateIsKnown(id, isKnown)

    }
}



