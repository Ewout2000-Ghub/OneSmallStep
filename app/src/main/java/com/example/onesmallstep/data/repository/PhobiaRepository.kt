package com.example.onesmallstep.data.repository

import androidx.lifecycle.LiveData
import com.example.onesmallstep.data.database.dao.PhobiaDao
import com.example.onesmallstep.data.database.dao.ProgressDao
import com.example.onesmallstep.data.entities.*

class PhobiaRepository(
    private val phobiaDao: PhobiaDao,
    private val progressDao: ProgressDao
) {

    fun getAllPhobias(): LiveData<List<Phobia>> = phobiaDao.getAllPhobias()

    fun getPhobiasByCategory(category: String): LiveData<List<Phobia>> =
        phobiaDao.getPhobiasByCategory(category)

    fun searchPhobias(query: String): LiveData<List<Phobia>> =
        phobiaDao.searchPhobias(query)

    suspend fun getPhobiaById(id: Int): Phobia? = phobiaDao.getPhobiaById(id)

    suspend fun getLevelsForPhobia(phobiaId: Int): List<ExposureLevel> =
        phobiaDao.getLevelsForPhobia(phobiaId)

    suspend fun getStepsForLevel(levelId: Int): List<ExposureStep> =
        phobiaDao.getStepsForLevel(levelId)

    fun getProgressForPhobia(phobiaId: Int): LiveData<List<UserProgress>> =
        progressDao.getProgressForPhobia(phobiaId)

    // ADD THIS MISSING METHOD:
    fun getCompletedSteps(): LiveData<List<UserProgress>> =
        progressDao.getCompletedSteps()

    suspend fun updateProgress(progress: UserProgress) =
        progressDao.updateProgress(progress)

    suspend fun insertProgress(progress: UserProgress) =
        progressDao.insertProgress(progress)

    suspend fun updatePhobia(phobia: Phobia) =
        phobiaDao.updatePhobia(phobia)

    suspend fun getCompletedStepsCount(phobiaId: Int): Int =
        progressDao.getCompletedStepsCount(phobiaId)

    suspend fun getTotalStepsCount(phobiaId: Int): Int =
        progressDao.getTotalStepsCount(phobiaId)
}
