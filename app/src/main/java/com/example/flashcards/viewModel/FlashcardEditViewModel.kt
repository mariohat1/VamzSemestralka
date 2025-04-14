package com.example.flashcards.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.EditFlashcardScreen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class FlashcardEditViewModel(
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])
    private val answerTextF = MutableStateFlow(savedStateHandle.get<String>("answer")?:"")
    val answerText: StateFlow<String> = answerTextF.asStateFlow()

    private val questionTextF = MutableStateFlow(savedStateHandle.get<String>("question")?:"")
    val questionText: StateFlow<String> = questionTextF.asStateFlow()

    private val flashcardId: Int = checkNotNull(savedStateHandle["flashcardId"])
    private val editFlashcardScreenState = MutableStateFlow(EditFlashcardScreen())

    val flashcardScreenState: StateFlow<EditFlashcardScreen> = editFlashcardScreenState

    init {

        if (flashcardId != -1) {
            viewModelScope.launch {
                flashcardRepository.getFlashcardById(flashcardId)
                    .filterNotNull()
                    .combine(deckRepository.getDeck(deckId).filterNotNull()) { flashcard, deck ->
                        EditFlashcardScreen(
                            flashcard = flashcard,
                            deckTitle = deck.deck.name,
                            isLoading = false
                        )
                    }.collect { screenState ->
                        Log.d("EditVM", "Emitting screenState: ${screenState.flashcard}") // Add this log
                        editFlashcardScreenState.value = screenState
                    }
            }
        } else {

            viewModelScope.launch {

                deckRepository.getDeck(deckId).filterNotNull()
                    .collect { deck ->
                        Log.d("FlashcardEditViewModel", "New Flashcard - Deck: $deck")
                        editFlashcardScreenState.value = EditFlashcardScreen(
                            flashcard = Flashcard(
                                deckId = deckId,
                                question = "",
                                answer = ""
                            ),
                            deckTitle = deck.deck.name
                        )
                    }
            }
        }

        Log.d("FlashcardEditViewModel", "Deck ID: $deckId, Flashcard ID: $flashcardId")
    }

    suspend fun saveFlashcard(question: String, answer: String) {
        if (flashcardId == -1) {
            flashcardRepository.insertFlashcard(
                Flashcard(deckId = deckId, question = question, answer = answer)
            )
        } else {
            flashcardRepository.updateFlashcard(
                Flashcard(
                    deckId = deckId,
                    question = question,
                    answer = answer,
                    flashcardId = flashcardId
                )
            )
        }
    }


    suspend fun delete(flashcard: Flashcard) {
        flashcardRepository.deleteFlashcard(flashcard)

    }


}