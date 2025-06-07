package com.example.flashcards.data.states

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards

data class EditDeckState(
    val deck: DeckWithFlashcards = DeckWithFlashcards(
        deck = Deck(
            deckId = 0,
            name = ""
        ),
        flashcards = listOf()
    ),
    val isLoading :Boolean= true
)


