package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard")
data class Flashcard(
    @PrimaryKey(autoGenerate =true)
    val flashcardId:Int,
    val deckId: Int,
    val question: String,
    val answer: String
)
