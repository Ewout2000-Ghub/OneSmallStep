package com.example.onesmallstep.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.onesmallstep.data.entities.UserProgress

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE phobiaId = :phobiaId")
    fun getProgressForPhobia(phobiaId: Int): LiveData<List<UserProgress>>

    @Query("SELECT * FROM user_progress WHERE isCompleted = 1")
    fun getCompletedSteps(): LiveData<List<UserProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgress)

    @Update
    suspend fun updateProgress(progress: UserProgress)

    @Query("SELECT COUNT(*) FROM user_progress WHERE phobiaId = :phobiaId AND isCompleted = 1")
    suspend fun getCompletedStepsCount(phobiaId: Int): Int

    @Query("SELECT COUNT(*) FROM exposure_steps WHERE levelId IN (SELECT id FROM exposure_levels WHERE phobiaId = :phobiaId)")
    suspend fun getTotalStepsCount(phobiaId: Int): Int

    @Query("DELETE FROM user_progress WHERE phobiaId = :phobiaId")
    suspend fun deleteProgressForPhobia(phobiaId: Int)
}