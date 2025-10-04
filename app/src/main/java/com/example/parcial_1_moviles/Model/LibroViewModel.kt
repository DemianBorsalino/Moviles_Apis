package com.example.parcial_1_moviles.Model

import android.util.Log
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
        val correctedQuery = autocorrectQuery(query)
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
            } catch (e: retrofit2.HttpException) {
                // Errores HTTP específicos
                val code = e.code()
                Log.e("BookViewModel", "HTTP error $code: ${e.message()}")

                _uiState.value = UiState.Error("Error al cargar libros", e)
            } catch (e: java.net.UnknownHostException) {
                // Sin conexión a Internet
                Log.e("BookViewModel", "Sin conexión a Internet", e)
                _uiState.value = UiState.Error("No hay conexión a Internet", e)
            } catch (e: Exception) {
                // Otros errores genéricos
                Log.e("BookViewModel", "Error inesperado", e)
                _uiState.value = UiState.Error("Error al cargar libros", e)
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
