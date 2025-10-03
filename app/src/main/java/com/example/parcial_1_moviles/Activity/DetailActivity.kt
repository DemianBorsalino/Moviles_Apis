package com.example.parcial_1_moviles.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.parcial_1_moviles.Model.Book
import com.example.parcial_1_moviles.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the Book object from the intent
        val book = intent.getParcelableExtra<Book>("book")

        if (book != null) {
            bindBookData(book)
        } else {
            binding.tvTitle.text = "No book data available"
        }
        binding.btnBack.setOnClickListener {
            finish()  // Cierra DetailActivity y vuelve a MainActivity
        }
    }

    private fun bindBookData(book: Book) {
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = book.author ?: "Unknown author"
        binding.tvYear.text = book.year?.toString() ?: "No year available"
        binding.tvEbookAccess.text = "Ebook access: ${book.ebookAccess ?: "Not available"}"
        binding.tvEditionCount.text = "Edition count: ${book.editionCount ?: 0}"
        binding.tvLanguage.text = "Languages: " + (book.language?.joinToString(", ") ?: "Unknown")

        // Load the cover image if available
        if (!book.coverUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(book.coverUrl)
                .into(binding.ivCover)
        } else {
            binding.ivCover.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }



}
