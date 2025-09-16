package com.example.onesmallstep.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exposure_levels")
data class ExposureLevel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phobiaId: Int,
    val levelNumber: Int, // 1-4 (Mild, Medium, Strong, Real-world)
    val title: String,
    val description: String,
    val estimatedDuration: String // e.g., "1-2 weeks"
)