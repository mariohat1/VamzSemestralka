package com.example.flashcards.data.states

import com.example.flashcards.data.entities.Flashcard

data class PlayState(
    val flashcards: List<Flashcard> = listOf(),
    val deckTitle:String = "",

)
