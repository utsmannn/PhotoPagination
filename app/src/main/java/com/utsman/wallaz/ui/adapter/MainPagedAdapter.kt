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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
        holder.setIsRecyclable(false)
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
        Glide.with(context)
                .load(photo.url.small)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(photo_image_view)

        val extras = FragmentNavigatorExtras(photo_image_view to "image_view")

        card_container.setOnClickListener {
            val bundle = bundleOf("photo_id" to photo.id)
            findNavController().navigate(R.id.toPhotoFragment, bundle, null, extras)
        }

    }
}