package com.example.flashcards.data.states


import com.example.flashcards.data.entities.Flashcard

data class EditFlashcardScreen(
    val flashcard: Flashcard = Flashcard(
        deckId = 0,
        question = "",
        answer = ""
    ),

    val deckTitle: String = ""



)