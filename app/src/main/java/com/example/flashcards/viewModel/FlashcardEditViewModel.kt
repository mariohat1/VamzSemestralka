package com.example.flashcards.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.entities.Flashcard
import com.example.flashcards.data.repository.FlashcardRepository
import com.example.flashcards.data.states.EditFlashcardScreen
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashcardEditViewModel(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val deckId: Int = checkNotNull(savedStateHandle["deckId"])
    private val flashcardId: Int = checkNotNull(savedStateHandle["flashcardId"])
    private val editFlashcardScreenState = MutableStateFlow(EditFlashcardScreen())
    val flashcardScreenState: StateFlow<EditFlashcardScreen> = editFlashcardScreenState

    init {
        if (flashcardId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val flashcard = flashcardRepository.getFlashcardById(flashcardId)
                withContext(Dispatchers.Main) {

                    editFlashcardScreenState.value = EditFlashcardScreen(flashcard = flashcard)
                }
            }
        } else {

            editFlashcardScreenState.value = EditFlashcardScreen(
                flashcard = Flashcard(
                    deckId = deckId,
                    question = "",
                    answer = ""
                )
            )
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