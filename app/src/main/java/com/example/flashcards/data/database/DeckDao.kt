package com.example.flashcards.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import com.example.flashcards.data.entities.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insertDeck(deck: Deck)

    @Update
    suspend fun updateDeck(deck: Deck)

    @Delete
     suspend fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM Deck")
     fun getAllDecks(): Flow<List<Deck>>

    @Transaction
    @Query("SELECT * FROM Deck Where deckId = :deckId")
     fun getDecksWithFlashcards(deckId: Int): Flow<List<DeckWithFlashcards>>

    @Transaction
    @Query("SELECT * FROM Deck ")
     fun getDecksWithFlashcards(): Flow<List<DeckWithFlashcards>>
}