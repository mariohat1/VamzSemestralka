package com.example.flashcards.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.data.entities.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM Flashcard")
     fun getAllFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM Flashcard WHERE flashcardId = :flashcardId")
     fun getFlashcardById(flashcardId: Int): Flow<Flashcard>

    @Query("SELECT * FROM Flashcard WHERE deckId = :deckId")
    fun getFlashcardsById(deckId: Int): Flow<List<Flashcard>>

    @Query("DELETE FROM flashcard WHERE deckId = :deckId")
    suspend fun deleteFlashcardsByDeckId(deckId: Int)
}