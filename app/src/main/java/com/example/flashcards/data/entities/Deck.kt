package com.example.flashcards.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "deck")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val deckId: Int = 0,
    @ColumnInfo(name = "deck_name")val name: String
)
