package com.example.onesmallstep.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.onesmallstep.data.entities.*

@Dao
interface PhobiaDao {
    @Query("SELECT * FROM phobias ORDER BY name ASC")
    fun getAllPhobias(): LiveData<List<Phobia>>

    @Query("SELECT * FROM phobias WHERE category = :category ORDER BY name ASC")
    fun getPhobiasByCategory(category: String): LiveData<List<Phobia>>

    @Query("SELECT * FROM phobias WHERE name LIKE '%' || :searchQuery || '%' OR scientificName LIKE '%' || :searchQuery || '%'")
    fun searchPhobias(searchQuery: String): LiveData<List<Phobia>>

    @Query("SELECT * FROM phobias WHERE id = :phobiaId")
    suspend fun getPhobiaById(phobiaId: Int): Phobia?

    @Query("SELECT * FROM exposure_levels WHERE phobiaId = :phobiaId ORDER BY levelNumber ASC")
    suspend fun getLevelsForPhobia(phobiaId: Int): List<ExposureLevel>

    @Query("SELECT * FROM exposure_steps WHERE levelId = :levelId ORDER BY stepNumber ASC")
    suspend fun getStepsForLevel(levelId: Int): List<ExposureStep>

    @Update
    suspend fun updatePhobia(phobia: Phobia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhobia(phobia: Phobia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: ExposureLevel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: ExposureStep)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhobias(phobias: List<Phobia>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<ExposureLevel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<ExposureStep>)
}