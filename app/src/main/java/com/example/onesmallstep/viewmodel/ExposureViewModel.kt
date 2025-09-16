package com.example.onesmallstep.viewmodel

import androidx.lifecycle.*
import com.example.onesmallstep.data.entities.*
import com.example.onesmallstep.data.repository.PhobiaRepository
import kotlinx.coroutines.launch

class ExposureViewModel(private val repository: PhobiaRepository) : ViewModel() {

    private val _exposureLevel = MutableLiveData<ExposureLevel?>()
    val exposureLevel: LiveData<ExposureLevel?> = _exposureLevel

    private val _steps = MutableLiveData<List<ExposureStep>>()
    val steps: LiveData<List<ExposureStep>> = _steps

    fun loadStepsForLevel(levelId: Int) {
        viewModelScope.launch {
            _steps.value = repository.getStepsForLevel(levelId)
        }
    }

    fun saveProgress(progress: UserProgress) {
        viewModelScope.launch {
            repository.insertProgress(progress)
        }
    }
}