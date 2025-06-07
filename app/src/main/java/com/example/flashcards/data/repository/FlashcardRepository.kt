package com.example.flashcards.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.data.database.FlashcardDao
import com.example.flashcards.data.entities.Flashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    suspend fun insertFlashcard(flashcard: Flashcard) {

        flashcardDao.insertFlashcard(flashcard)


    }


    suspend fun deleteFlashcard(flashcard: Flashcard) {

        flashcardDao.deleteFlashcard(flashcard)

    }


    suspend fun updateFlashcard(flashcard: Flashcard) {

        flashcardDao.updateFlashcard(flashcard)

    }

    fun getUknownFlashcardsByDeckId(deckId: Int): Flow<List<Flashcard>> {
        return flashcardDao.getUnknownFlashcardsByDeckId(deckId)

    }

    suspend fun updateIsKnown(id: Int, isKnown: Boolean) {

        flashcardDao.updateIsKnown(id, isKnown)

    }


    suspend fun deleteFlashcardsByDeckId(deckId: Int) {
        flashcardDao.deleteFlashcardsByDeckId(deckId)
    }


    fun getFlashcardById(flashcardId: Int): Flow<Flashcard> {

        return flashcardDao.getFlashcardById(flashcardId)
    }


}