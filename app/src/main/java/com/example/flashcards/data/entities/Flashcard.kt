package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flashcard(
    @PrimaryKey
    val flashcardId:Long,
    val question: String,
    val answer: String
)
