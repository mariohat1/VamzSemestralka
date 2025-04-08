package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Deck(
    @PrimaryKey
    val deckId: Long,
    val flashcardId : Long,
    val name: String
)
