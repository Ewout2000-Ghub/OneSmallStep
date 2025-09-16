package com.example.onesmallstep.viewmodel

import androidx.lifecycle.*
import com.example.onesmallstep.data.entities.*
import com.example.onesmallstep.data.repository.PhobiaRepository
import kotlinx.coroutines.launch

class PhobiaDetailViewModel(private val repository: PhobiaRepository) : ViewModel() {

    private val _phobia = MutableLiveData<Phobia?>()
    val phobia: LiveData<Phobia?> = _phobia

    private val _levels = MutableLiveData<List<ExposureLevel>>()
    val levels: LiveData<List<ExposureLevel>> = _levels

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _progressData = MutableLiveData<List<UserProgress>>()
    val progressData: LiveData<List<UserProgress>> = _progressData

    private val _overallProgress = MutableLiveData<Int>()
    val overallProgress: LiveData<Int> = _overallProgress

    fun loadPhobiaDetails(phobiaId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val phobiaData = repository.getPhobiaById(phobiaId)
                _phobia.value = phobiaData

                if (phobiaData == null) {
                    _errorMessage.value = "Phobia not found"
                    _isLoading.value = false
                    return@launch
                }

                val levelsData = repository.getLevelsForPhobia(phobiaId)
                _levels.value = levelsData

                loadProgressData(phobiaId)
                _isLoading.value = false

            } catch (e: Exception) {
                _errorMessage.value = "Error loading phobia details: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private fun loadProgressData(phobiaId: Int) {
        repository.getProgressForPhobia(phobiaId).observeForever { progressList ->
            _progressData.value = progressList
            calculateOverallProgress(phobiaId)
        }
    }

    private fun calculateOverallProgress(phobiaId: Int) {
        viewModelScope.launch {
            val completed = repository.getCompletedStepsCount(phobiaId)
            val total = repository.getTotalStepsCount(phobiaId)
            val percentage = if (total > 0) (completed * 100) / total else 0
            _overallProgress.value = percentage
        }
    }
}
