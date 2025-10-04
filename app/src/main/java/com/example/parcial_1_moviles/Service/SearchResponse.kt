package com.example.parcial_1_moviles.Model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("docs")
    val docs: List<DocDto> = emptyList()
)
