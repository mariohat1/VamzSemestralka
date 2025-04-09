package com.example.flashcards.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.data.database.FlashcardDao
import com.example.flashcards.data.entities.Flashcard
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val  flashcardDao: FlashcardDao) {

    fun insertFlashcard(flashcard: Flashcard) {
        flashcardDao.insertFlashcard(flashcard)
    }


    fun deleteFlashcard(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }


    fun updateFlashcard(flashcard: Flashcard) {
        flashcardDao.updateFlashcard(flashcard)
    }


    fun getAllFlashcards(): Flow<List<Flashcard>> {

        return  flashcardDao.getAllFlashcards()
    }


    fun getFlashcardById(flashcardId: Int): Flashcard {

        return flashcardDao.getFlashcardById(flashcardId)
    }

}