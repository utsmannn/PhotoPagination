package com.utsman.wallaz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.wallaz.R
import com.utsman.wallaz.data.Photos
import kotlinx.android.synthetic.main.item_photo_card.view.*


class MainPagedAdapter : PagedListAdapter<Photos, MainViewHolder>(photoDiffUtil) {

    private fun view(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(view(parent))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) holder.bind(photo)
    }

    companion object {
        private val photoDiffUtil = object : DiffUtil.ItemCallback<Photos>() {
            override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean = oldItem == newItem

        }
    }
}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(photo: Photos) = itemView.run {
        Picasso.get().load(photo.url.regular).into(photo_image_view)

        card_container.setOnClickListener {
            val bundle = bundleOf("photo" to photo)
            findNavController().navigate(R.id.toPhotoFragment, bundle)
        }
    }
}