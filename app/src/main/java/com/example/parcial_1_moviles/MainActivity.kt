package com.example.parcial_1_moviles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.parcial_1_moviles.Model.LibroAdapter
import com.example.parcial_1_moviles.Model.LibroVMFactory
import com.example.parcial_1_moviles.Model.LibroViewModel
import com.example.parcial_1_moviles.Service.RepositorioLibros
import com.example.parcial_1_moviles.Service.RetrofitClient
import com.example.parcial_1_moviles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // keep last query to support retry
    private var lastQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create repository & viewModel via factory
        val api = RetrofitClient.api
        val repository = RepositorioLibros(api)
        val factory = LibroVMFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(LibroViewModel::class.java)

        val adapter = LibroAdapter { book ->
            // English comment: handle item click -> navigate to detail screen (to implement)
            Toast.makeText(this, "Clicked: ${book.title}", Toast.LENGTH_SHORT).show()
            // Intent to DetailActivity can be added here passing book.id
        }

        binding.rvBooks.adapter = adapter
        binding.rvBooks.layoutManager = LinearLayoutManager(this)

        // Observe the view state and update UI accordingly.
        viewModel.booksState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvBooks.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvBooks.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                    adapter.submitList(state.data)
                }
                is UiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvBooks.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvBooks.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvError.text = state.message
                }
            }
        }

        // SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val q = query?.trim().orEmpty()
                if (q.isNotEmpty()) {
                    lastQuery = q
                    viewModel.searchBooks(q)
                }
                binding.searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        // Retry button
        binding.btnRetry.setOnClickListener {
            if (lastQuery.isNotEmpty()) {
                viewModel.searchBooks(lastQuery)
            } else {
                Toast.makeText(this, "Please enter a query", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
