package com.example.parcial_1_moviles.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository = RepositorioLibros(RetrofitClient.api)
    private val factory = LibroViewModelFactory(repository)
    private val viewModel: LibroViewModel by viewModels { factory }

    private lateinit var adapter: LibroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupRetryButton()
        setupSearchView()

        // Búsqueda inicial por defecto
        viewModel.searchBooks("harry potter")
    }

    private fun setupSearchView() { //Agregé esta función que permite justamente el buscador
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        viewModel.searchBooks(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Si querés búsqueda en tiempo real, podés descomentar:
                // if (!newText.isNullOrBlank()) viewModel.searchBooks(newText)
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = LibroAdapter { book -> navigateToDetail(book) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
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
                is UiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.tvEmpty.text = "Error al cargar los datos"
                }
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
