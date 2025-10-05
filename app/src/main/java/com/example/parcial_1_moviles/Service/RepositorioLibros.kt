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
            Log.e("RepositorioLibros", "Error HTTP ${e.code()} al buscar libros", e)
            throw e
        } catch (e: IOException) {
            Log.e("RepositorioLibros", "Error de red al buscar libros", e)
            throw e
        } catch (e: Exception) {
            Log.e("RepositorioLibros", "Error inesperado", e)
            throw e
        }
    }
}
