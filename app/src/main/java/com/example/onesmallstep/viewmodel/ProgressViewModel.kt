package com.example.onesmallstep.viewmodel

import androidx.lifecycle.*
import com.example.onesmallstep.data.entities.*
import com.example.onesmallstep.data.repository.PhobiaRepository
import kotlinx.coroutines.launch

class ProgressViewModel(private val repository: PhobiaRepository) : ViewModel() {

    private val _activePhobias = MutableLiveData<List<Phobia>>()
    val activePhobias: LiveData<List<Phobia>> = _activePhobias

    private val _completedSteps = MutableLiveData<List<UserProgress>>()
    val completedSteps: LiveData<List<UserProgress>> = _completedSteps

    private val _totalStepsCompleted = MutableLiveData<Int>()
    val totalStepsCompleted: LiveData<Int> = _totalStepsCompleted

    private val _currentStreak = MutableLiveData<Int>()
    val currentStreak: LiveData<Int> = _currentStreak

    private val _weeklyProgress = MutableLiveData<List<UserProgress>>()
    val weeklyProgress: LiveData<List<UserProgress>> = _weeklyProgress

    private val _motivationalMessage = MutableLiveData<String>()
    val motivationalMessage: LiveData<String> = _motivationalMessage

    fun loadProgressData() {
        // Load completed steps
        repository.getCompletedSteps().observeForever { steps ->
            _completedSteps.value = steps
            _totalStepsCompleted.value = steps.size
            calculateStreak(steps)
            calculateWeeklyProgress(steps)
            updateMotivationalMessage(steps.size, _currentStreak.value ?: 0)
        }

        // Load active phobias
        repository.getAllPhobias().observeForever { phobias ->
            _activePhobias.value = phobias.filter { it.isActive }
        }
    }

    private fun calculateStreak(progressList: List<UserProgress>) {
        viewModelScope.launch {
            val streak = calculateConsecutiveDays(progressList)
            _currentStreak.value = streak
        }
    }

    private fun calculateConsecutiveDays(progressList: List<UserProgress>): Int {
        if (progressList.isEmpty()) return 0

        val sortedProgress = progressList
            .filter { it.isCompleted && it.completedDate != null }
            .sortedByDescending { it.completedDate }

        if (sortedProgress.isEmpty()) return 0

        var streak = 0
        var lastDate = java.util.Date()

        for (progress in sortedProgress) {
            val completedDate = progress.completedDate ?: break
            val daysDiff = ((lastDate.time - completedDate.time) / (1000 * 60 * 60 * 24)).toInt()

            if (daysDiff <= 1) {
                streak++
                lastDate = completedDate
            } else {
                break
            }
        }

        return streak
    }

    private fun calculateWeeklyProgress(progressList: List<UserProgress>) {
        val oneWeekAgo = java.util.Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000))
        val weeklySteps = progressList.filter {
            it.completedDate != null && it.completedDate!! >= oneWeekAgo
        }
        _weeklyProgress.value = weeklySteps
    }

    private fun updateMotivationalMessage(completedSteps: Int, streak: Int) {
        val message = when {
            streak == 0 && completedSteps == 0 -> "Ready to take your first step?"
            streak in 1..2 -> "Great start! Keep going!"
            streak in 3..6 -> "You're building a great habit!"
            streak in 7..13 -> "Amazing progress! You're doing fantastic!"
            streak >= 14 -> "Incredible dedication! You're conquering your fears!"
            else -> "Keep up the excellent work!"
        }
        _motivationalMessage.value = message
    }

    fun getProgressPercentageForPhobia(phobiaId: Int): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            val completed = repository.getCompletedStepsCount(phobiaId)
            val total = repository.getTotalStepsCount(phobiaId)
            val percentage = if (total > 0) (completed * 100) / total else 0
            result.postValue(percentage)
        }
        return result
    }

    fun resetProgressForPhobia(phobiaId: Int) {
        viewModelScope.launch {
            // Implement reset functionality if needed
        }
    }
}