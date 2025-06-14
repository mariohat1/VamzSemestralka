package com.example.flashcards.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.LengthConstants
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.repository.DeckRepository
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.FlashcardEditState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class FlashcardEditViewModel(
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])
    var questionText = MutableStateFlow("")
    private set
    var answerText = MutableStateFlow("")
    private set

    private val flashcardId: Int = checkNotNull(savedStateHandle["flashcardId"])
    private val flashcardEditStateState = MutableStateFlow(FlashcardEditState())
    var flashcardScreenState: StateFlow<FlashcardEditState> = flashcardEditStateState
    private set

    init {
        if (flashcardId != -1) {
            viewModelScope.launch {
                flashcardRepository.getFlashcardById(flashcardId)
                    .filterNotNull()
                    .combine(deckRepository.getDeck(deckId).filterNotNull()) { flashcard, deck ->
                        FlashcardEditState(
                            flashcard = flashcard,
                            deckTitle = deck.deck.name,
                            isLoading = false
                        )
                    }.collect { screenState ->
                        flashcardEditStateState.value = screenState
                        questionText.value = flashcardEditStateState.value.flashcard.question
                        answerText.value = flashcardEditStateState.value.flashcard.answer
                    }
            }

        } else {
            viewModelScope.launch {
                deckRepository.getDeck(deckId).filterNotNull()
                    .collect { deck ->
                        Log.d("FlashcardEditViewModel", "New Flashcard - Deck: $deck")
                        flashcardEditStateState.value = FlashcardEditState(
                            flashcard = Flashcard(
                                deckId = deckId,
                                question = "",
                                answer = ""
                            ),
                            deckTitle = deck.deck.name
                        )
                    }
            }
            questionText.value = ""
            answerText.value = ""



        }

    }
    fun onQuestionChanged(newValue: String) {
        if (newValue.length <= LengthConstants.MAX_DECK_NAME_LENGTH) {
            questionText.value = newValue
        }
    }

    fun onAnswerChanged(newValue: String) {
        if (newValue.length <= LengthConstants.MAX_CARD_QUESTION_LENGTH) {
            answerText.value = newValue
        }
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
