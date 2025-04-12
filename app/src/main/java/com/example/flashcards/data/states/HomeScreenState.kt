package com.example.flashcards.data.states

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards

data class HomeScreenState(
    val decks: List<DeckWithFlashcards> = listOf(),
    val isLoading:Boolean = true,

    )
