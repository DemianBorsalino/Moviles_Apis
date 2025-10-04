package com.example.parcial_1_moviles.Model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial_1_moviles.R
import com.example.parcial_1_moviles.databinding.ItemsBinding
import com.bumptech.glide.Glide


class LibroAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onFavoriteClick: (Book) -> Unit
) : ListAdapter<Book, LibroAdapter.BookViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(old: Book, new: Book) = old.id == new.id
            override fun areContentsTheSame(old: Book, new: Book) = old == new
        }
    }

    inner class BookViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book) {
            // Set book data
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.author ?: "Unknown author"
            binding.tvYear.text = item.year?.toString() ?: ""

            // Load cover
            Glide.with(binding.root.context)
                .load(item.coverUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivCover)

            val starIcon = if (item.isFavorite) {
                R.drawable.ic_star_filled
            } else {
                R.drawable.ic_star_border
            }
            binding.ivFavorite.setImageResource(starIcon)

            // ✅Click en item → Detalle
            binding.root.setOnClickListener {
                onItemClick(item)
            }

            // ✅ Click en la estrella → Favorito
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
