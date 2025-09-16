package com.example.onesmallstep.viewmodel

import androidx.lifecycle.*
import com.example.onesmallstep.data.entities.Phobia
import com.example.onesmallstep.data.repository.PhobiaRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PhobiaRepository) : ViewModel() {

    private val _phobias = MutableLiveData<List<Phobia>>()
    val phobias: LiveData<List<Phobia>> = _phobias

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadAllPhobias()
        _selectedCategory.value = "all"
    }

    fun loadAllPhobias() {
        _isLoading.value = true
        _selectedCategory.value = "all"
        _searchQuery.value = ""

        repository.getAllPhobias().observeForever { phobiaList ->
            _phobias.value = phobiaList
            _isLoading.value = false
            if (phobiaList.isEmpty()) {
                _errorMessage.value = "No phobias found. Please check your connection."
            } else {
                _errorMessage.value = null
            }
        }
    }

    fun loadPhobiasByCategory(category: String) {
        _isLoading.value = true
        _selectedCategory.value = category
        _searchQuery.value = ""

        if (category == "all") {
            loadAllPhobias()
            return
        }

        repository.getPhobiasByCategory(category).observeForever { phobiaList ->
            _phobias.value = phobiaList
            _isLoading.value = false
            _errorMessage.value = null
        }
    }

    fun searchPhobias(query: String) {
        _isLoading.value = true
        _searchQuery.value = query

        if (query.isBlank()) {
            when (_selectedCategory.value) {
                "all" -> loadAllPhobias()
                else -> _selectedCategory.value?.let { loadPhobiasByCategory(it) }
            }
            return
        }

        repository.searchPhobias(query).observeForever { phobiaList ->
            _phobias.value = phobiaList
            _isLoading.value = false
            if (phobiaList.isEmpty()) {
                _errorMessage.value = "No phobias found matching '$query'"
            } else {
                _errorMessage.value = null
            }
        }
    }

    fun togglePhobiaActive(phobia: Phobia) {
        viewModelScope.launch {
            val updatedPhobia = phobia.copy(isActive = !phobia.isActive)
            repository.updatePhobia(updatedPhobia)
            when {
                !_searchQuery.value.isNullOrBlank() -> searchPhobias(_searchQuery.value!!)
                _selectedCategory.value == "all" -> loadAllPhobias()
                else -> _selectedCategory.value?.let { loadPhobiasByCategory(it) }
            }
        }
    }

    fun getPhobiaById(phobiaId: Int): LiveData<Phobia?> {
        val result = MutableLiveData<Phobia?>()
        viewModelScope.launch {
            result.value = repository.getPhobiaById(phobiaId)
        }
        return result
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun refreshData() {
        when {
            !_searchQuery.value.isNullOrBlank() -> searchPhobias(_searchQuery.value!!)
            _selectedCategory.value == "all" -> loadAllPhobias()
            else -> _selectedCategory.value?.let { loadPhobiasByCategory(it) }
        }
    }
}
