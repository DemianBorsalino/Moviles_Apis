package com.example.parcial_1_moviles.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parcial_1_moviles.Model.Book
import com.example.parcial_1_moviles.Model.LibroAdapter
import com.example.parcial_1_moviles.Model.LibroViewModel
import com.example.parcial_1_moviles.Model.LibroViewModelFactory
import com.example.parcial_1_moviles.Service.RepositorioLibros
import com.example.parcial_1_moviles.Service.RetrofitClient
import com.example.parcial_1_moviles.UiState
import com.example.parcial_1_moviles.databinding.ActivityMainBinding
import com.example.parcial_1_moviles.ui.detail.DetailActivity

class   MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val repository = RepositorioLibros(RetrofitClient.api)
    val factory = LibroViewModelFactory(repository)
    private val viewModel: LibroViewModel by viewModels { factory }

    private lateinit var adapter: LibroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupRetryButton()

        // Trigger API call with default query
        viewModel.searchBooks("harry potter")
    }

    private fun setupRecyclerView() {
        adapter = LibroAdapter { book -> navigateToDetail(book) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> { /* mostrar progress */ }
                is UiState.Success -> {
                    val books = state.data as? List<Book> ?: emptyList()
                    if (books.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        adapter.submitList(books)
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
                is UiState.Empty -> { /* mostrar empty view */ }
                is UiState.Error -> { /* mostrar error */ }
            }
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.retryLastSearch()
        }
    }

    private fun navigateToDetail(book: Book) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("book", book)
        startActivity(intent)
    }
}