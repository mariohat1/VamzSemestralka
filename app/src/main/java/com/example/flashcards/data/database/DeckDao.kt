package com.example.flashcards.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
     fun insertDeck(deck: Deck)

    @Update
     fun updateDeck(deck: Deck)

    @Delete
     fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM Deck")
    fun getAllDecks(): Flow<List<Deck>>

    @Transaction
    @Query("SELECT * FROM Deck Where deckId = :deckId")
    fun getDecksWithFlashcards(deckId: Long): Flow<List<DeckWithFlashcards>>

    @Transaction
    @Query("SELECT * FROM Deck ")
    fun getDecksWithFlashcards(): Flow<List<DeckWithFlashcards>>
}