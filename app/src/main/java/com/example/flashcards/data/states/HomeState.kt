package com.example.flashcards.data.states

import com.example.flashcards.data.entities.DeckWithFlashcards



data class HomeState(
    val decks: List<DeckWithFlashcards> = emptyList(),
    val isLoading: Boolean = true
    )
