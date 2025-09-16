package com.example.onesmallstep.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exposure_steps")
data class ExposureStep(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val levelId: Int,
    val stepNumber: Int,
    val title: String,
    val description: String,
    val instructions: String,
    val duration: String, // e.g., "10 seconds", "5 minutes"
    val frequency: String // e.g., "Daily for 1 week"
)