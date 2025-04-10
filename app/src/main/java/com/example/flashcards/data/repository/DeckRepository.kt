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


    fun getDeck(deckId: Int): Flow<DeckWithFlashcards> {
        return deckDao.getDeck(deckId)
    }


    fun getDeckWithFlashcards(): Flow<List<DeckWithFlashcards>> {
        return deckDao.getDeckWithFlashcards()
    }
}