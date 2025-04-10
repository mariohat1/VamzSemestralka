package com.example.flashcards.data.repository

import com.example.flashcards.data.database.DeckDao
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.DeckWithFlashcards
import kotlinx.coroutines.flow.Flow

class DeckRepository(private val deckDao: DeckDao) {

    suspend fun insertDeck(deck: Deck){
        deckDao.insertDeck(deck);

    }


    suspend fun updateDeck(deck: Deck) {
        deckDao.updateDeck(deck)
    }


    suspend fun deleteDeck(deck: Deck) {
        deckDao.deleteDeck(deck)
    }


    fun getAllDecks(): Flow<List<Deck>> {
       return deckDao.getAllDecks()

    }


    fun getDeckWithFlashcards(deckId: Int): Flow<List<DeckWithFlashcards>> {
        return deckDao.getDecksWithFlashcards(deckId)
    }


    fun getDecksWithFlashcards(): Flow<List<DeckWithFlashcards>> {
        return deckDao.getDecksWithFlashcards()
    }
}