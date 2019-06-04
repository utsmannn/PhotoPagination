/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.wallaz.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.utsman.wallaz.R
import com.utsman.wallaz.bitMapToString
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
        holder.setIsRecyclable(false)
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
        Glide.with(context).asBitmap().load(photoRoom.url).into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                photo_image_view.setImageBitmap(resource)

                card_container.setOnClickListener {
                    val bundle = bundleOf("photo_id" to photoRoom.id)
                    findNavController().navigate(R.id.toPhotoFragmentBookmark, bundle)
                }
            }

        })
    }
}
