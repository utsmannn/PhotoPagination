package com.utsman.wallaz.ui.adapter

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
import com.utsman.wallaz.db.PhotoRoom
import kotlinx.android.synthetic.main.item_photo_card.view.*


class BookmarkPagedAdapter : PagedListAdapter<PhotoRoom, BookmarkViewHolder>(bookmarkDiffUtil) {
    private fun view(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(view(parent))
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) holder.bind(photo)
    }


    companion object {
        private val bookmarkDiffUtil = object : DiffUtil.ItemCallback<PhotoRoom>() {
            override fun areItemsTheSame(oldItem: PhotoRoom, newItem: PhotoRoom): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: PhotoRoom, newItem: PhotoRoom): Boolean = oldItem == newItem

        }
    }
}

class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(photoRoom: PhotoRoom) = itemView.run {
        Picasso.get().load(photoRoom.url).into(photo_image_view)

        card_container.setOnClickListener {
            val bundle = bundleOf("photo_id" to photoRoom.id)
            findNavController().navigate(R.id.toPhotoFragmentBookmark, bundle)
        }
    }
}
