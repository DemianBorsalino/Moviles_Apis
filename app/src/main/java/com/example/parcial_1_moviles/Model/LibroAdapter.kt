package com.example.parcial_1_moviles.Model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial_1_moviles.R
import com.example.parcial_1_moviles.databinding.ItemsBinding
import com.bumptech.glide.Glide


class LibroAdapter(private val onClick: (Book) -> Unit) :
    ListAdapter<Book, LibroAdapter.BookViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(old: Book, new: Book) = old.id == new.id
            override fun areContentsTheSame(old: Book, new: Book) = old == new
        }
    }

    inner class BookViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book) {
            // English comments required: bind data to the views.
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.author ?: "Unknown author"
            binding.tvYear.text = item.year?.toString() ?: ""
            // load image with Coil (no explicit colors or styles)
            //binding.ivCover.setImageResource(android.R.drawable.ic_menu_report_image) //Por si lo lográs hacer funcionar
            //item.coverUrl?.let { binding.ivCover.load(it) } //Quería ponerle una imágen pero no funcionaba no se por qué

            Glide.with(binding.root.context)
                .load(item.coverUrl)
                .placeholder(android.R.drawable.ic_menu_report_image) // placeholder while loading
                .error(android.R.drawable.ic_menu_report_image)       // fallback if error
                .into(binding.ivCover)


            binding.root.setOnClickListener {
                onClick(item)
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