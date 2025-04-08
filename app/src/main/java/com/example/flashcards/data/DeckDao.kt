package com.example.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards

@Dao
interface DeckDao {
    @Insert
    suspend fun insertDeck(deck: Deck)

    @Update
    suspend fun updateDeck(deck: Deck)

    @Delete
    suspend fun deleteDeck(deck: Deck)

    // Týmto získavame balíčky spolu s kartičkami
    @Transaction
    @Query("SELECT * FROM Deck Where deckId = :deckId")
    suspend fun getDecksWithFlashcards(deckId: Long): List<DeckWithFlashcards>
}