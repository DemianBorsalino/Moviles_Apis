package com.example.parcial_1_moviles.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parcial_1_moviles.Model.LibroAdapter
import com.example.parcial_1_moviles.Model.LibroViewModel
import com.example.parcial_1_moviles.databinding.ActivityFavoritesBinding


class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: LibroViewModel by viewModels { viewModelFactory {  } }
    private lateinit var adapter: LibroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LibroAdapter(
            onItemClick = { book -> /* navegar a detalle si querés */ },
            onFavoriteClick = { book -> viewModel.toggleFavorite(book); adapter.notifyDataSetChanged() }
        )

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = adapter

        viewModel.favorites.observe(this) { favList ->
            adapter.submitList(favList)
        }
    }
}
