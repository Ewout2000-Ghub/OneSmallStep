package com.example.onesmallstep.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phobias")
data class Phobia(
    @PrimaryKey
    val id: Int,
    val name: String,
    val scientificName: String,
    val description: String,
    val iconResource: String,
    val category: String, // "common" or "rare"
    val isActive: Boolean = false // user is working on this phobia
)