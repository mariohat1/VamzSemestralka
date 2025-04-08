package com.example.flashcards.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithFlashcards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "flashcardId",
        entityColumn = "flashcardId"
    )
    val flashcards: List<Flashcard>
)
