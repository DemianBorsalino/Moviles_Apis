package com.example.parcial_1_moviles.Model

import com.example.parcial_1_moviles.Activity.UiState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parcial_1_moviles.Service.RepositorioLibros
import kotlinx.coroutines.launch

class LibroViewModel(private val repository: RepositorioLibros) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<Book>>>()
    private val _favorites = MutableLiveData<MutableList<Book>>(mutableListOf())
    val favorites: MutableLiveData<MutableList<Book>> = _favorites
    val uiState: LiveData<UiState<List<Book>>> = _uiState

    private var lastQuery: String = ""

    fun searchBooks(query: String) {
        val correctedQuery = autocorrectQuery(query) // ✅ Corrección aplicada aquí
        lastQuery = correctedQuery
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val books = repository.searchBooks(correctedQuery)
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

    fun toggleFavorite(book: Book) {
        val list = _favorites.value ?: mutableListOf()

        if (list.any { it.id == book.id }) {
            list.removeAll { it.id == book.id }
            book.isFavorite = false
        } else {
            list.add(book)
            book.isFavorite = true
        }

        _favorites.value = list
    }

    // ✅ Función de autocorrección
    private fun autocorrectQuery(query: String): String {
        val replacements = mapOf(
            "rign" to "ring",
            "hary poter" to "harry potter",
            "tolken" to "tolkien"
        )

        val lower = query.lowercase()
        return replacements[lower] ?: query
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
