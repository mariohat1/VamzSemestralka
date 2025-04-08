package com.example.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.data.entities.Flashcard

@Dao
interface FlashcardDao {
    @Insert
    fun insertFlashcard(flashcard: Flashcard)

    @Delete
    fun deleteFlashcard(flashcard: Flashcard)

    @Update
    fun updateFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM Flashcard")
    suspend fun getAllFlashcards(): List<Flashcard>

    @Query("SELECT * FROM Flashcard WHERE flashcardId = :flashcardId")
    suspend fun getFlashcardById(flashcardId: Long): Flashcard
}