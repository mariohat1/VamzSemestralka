package com.example.flashcards.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Flashcard

@Database(entities = [Deck::class, Flashcard::class],
    version = 4)
abstract class FlaschcardDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun deckDao(): DeckDao
    companion object {


        @Volatile
        private var Instance: FlaschcardDatabase? = null

        fun getDatabase(context: Context): FlaschcardDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    FlaschcardDatabase::class.java,
                    "flashcard_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}