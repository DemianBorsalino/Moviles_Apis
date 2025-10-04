package com.example.parcial_1_moviles.Service

import android.util.Log
import com.example.parcial_1_moviles.Model.Book
import com.example.parcial_1_moviles.Model.toDomain
import retrofit2.HttpException
import java.io.IOException

class RepositorioLibros(private val api: OpenLibraryAPI) {
    suspend fun searchBooks(query: String): List<Book> {
        return try {
            val response = api.searchBooks(query)
            response.docs.map { it.toDomain() }
        } catch (e: HttpException) {
            // Errores HTTP: 4xx, 5xx
            Log.e("RepositorioLibros", "Error HTTP ${e.code()} al buscar libros", e)
            throw e
        } catch (e: IOException) {
            // Errores de red (sin conexión, timeout, etc.)
            Log.e("RepositorioLibros", "Error de red al buscar libros", e)
            throw e
        } catch (e: Exception) {
            // Cualquier otro error
            Log.e("RepositorioLibros", "Error inesperado", e)
            throw e
        }
    }
}
