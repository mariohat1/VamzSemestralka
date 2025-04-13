package com.example.flashcards.data.states

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard

data class PlayState(
    val flashcards:List<Flashcard> = listOf(),
    val deckTitle:String = "",
    val isLoading:Boolean = true
)