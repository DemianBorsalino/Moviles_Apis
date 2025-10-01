package com.example.parcial_1_moviles.Service

import  com.example.parcial_1_moviles.Model.Book
class RepositorioLibros (private val api: OpenLibraryAPI)  {
    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.docs.map { it.toDomain() }
    }
}