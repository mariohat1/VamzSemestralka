package com.example.flashcards.data.states


import com.example.flashcards.data.entities.Flashcard

data class FlashcardEditState(
    val flashcard: Flashcard = Flashcard(
        deckId = 0,
        question = "",
        answer = ""
    ),
    val isLoading: Boolean = true,
    val deckTitle: String = ""
)