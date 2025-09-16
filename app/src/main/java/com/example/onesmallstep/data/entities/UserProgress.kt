package com.example.onesmallstep.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phobiaId: Int,
    val levelId: Int,
    val stepId: Int,
    val isCompleted: Boolean,
    val anxietyRating: Int?, // 1-10 scale
    val completedDate: Date?,
    val notes: String?
)