package com.example.parcial_1_moviles.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial_1_moviles.Model.Book
import com.example.parcial_1_moviles.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val book = intent.getParcelableExtra<Book>("book")

        if (book != null) {
            bindBook(book)
        } else {
            binding.tvTitle.text = "No data"
        }
    }

    private fun bindBook(book: Book) {
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = book.author
        binding.tvYear.text = book.year?.toString()
    }
}
