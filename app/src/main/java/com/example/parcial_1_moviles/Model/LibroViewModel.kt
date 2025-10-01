package com.example.parcial_1_moviles.Model

import com.example.parcial_1_moviles.UiState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_1_moviles.Service.RepositorioLibros
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LibroViewModel(private val repository: RepositorioLibros): ViewModel() {

    private val _booksState = MutableLiveData<UiState<List<Book>>>()
    val booksState: LiveData<UiState<List<Book>>> = _booksState

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _booksState.value = UiState.Loading
            try {
                val list = repository.searchBooks(query)
                if (list.isEmpty()) {
                    _booksState.value = UiState.Empty
                   } else {
                    _booksState.value = UiState.Success(list)
                }
            } catch (io: IOException) {
                _booksState.value = UiState.Error("Network error: ${io.message}", io)
            } catch (http: HttpException) {
                _booksState.value = UiState.Error("API error: ${http.message}", http)
            } catch (e: Exception) {
                _booksState.value = UiState.Error("Unexpected error: ${e.localizedMessage}", e)
            }
        }
    }
}