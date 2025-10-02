package com.example.parcial_1_moviles.Service

import com.example.parcial_1_moviles.Model.Libro
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryAPI {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): Libro
}
