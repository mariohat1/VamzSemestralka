package com.example.flashcards.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Flashcard

@Database(entities = [Deck::class, Flashcard::class],
    version = 1)
abstract class Database : RoomDatabase() {
    abstract val daoFlashcard: FlashcardDao
    abstract val daoDeck: DeckDao
}