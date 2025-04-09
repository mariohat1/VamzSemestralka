package com.example.flashcards.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithFlashcards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "deckId"
    )
    val flashcards: List<Flashcard>
)