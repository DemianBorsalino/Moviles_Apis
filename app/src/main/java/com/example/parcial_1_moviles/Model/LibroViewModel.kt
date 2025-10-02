package com.example.parcial_1_moviles.Model

import com.example.parcial_1_moviles.UiState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parcial_1_moviles.Service.RepositorioLibros
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LibroViewModel(private val repository: RepositorioLibros) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<Book>>>()
    val uiState: LiveData<UiState<List<Book>>> = _uiState

    private var lastQuery: String = ""

    fun searchBooks(query: String) {
        lastQuery = query
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val books = repository.searchBooks(query)
                if (books.isEmpty()) {
                    _uiState.value = UiState.Empty
                } else {
                    _uiState.value = UiState.Success(books)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error", e)
            }
        }
    }

    fun retryLastSearch() {
        if (lastQuery.isNotEmpty()) searchBooks(lastQuery)
    }
}

// ViewModelFactory para poder pasar el repositorio
class LibroViewModelFactory(private val repository: RepositorioLibros) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}