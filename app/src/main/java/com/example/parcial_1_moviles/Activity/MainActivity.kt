package com.example.parcial_1_moviles.Activity

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
import com.example.parcial_1_moviles.Activity.UiState
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

        // Initial default search
        viewModel.searchBooks("harry potter")

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        // ✅ Aplicamos corrección antes de buscar
                        val correctedQuery = autocorrectQuery(it)
                        viewModel.searchBooks(correctedQuery)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    // ✅ Función de autocorrección
    private fun autocorrectQuery(query: String): String {
        val replacements = mapOf(
            "rign" to "ring",
            "hary poter" to "harry potter",
            "tolken" to "tolkien"
        )

        val lower = query.lowercase()
        return replacements[lower] ?: query
    }


    private fun setupRecyclerView() {
        adapter = LibroAdapter(
            onItemClick = { book -> navigateToDetail(book) },
            onFavoriteClick = { book -> viewModel.toggleFavorite(book); adapter.notifyDataSetChanged() }
        )
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