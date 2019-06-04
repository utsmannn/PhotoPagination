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

package com.utsman.wallaz.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.wallaz.R
import com.utsman.wallaz.textWithCheckEmptySetDefaultOrNol
import com.utsman.wallaz.textWithCheckEmptySetDefaultOrNolOrUnique
import com.utsman.wallaz.ui.CameraInfo
import kotlinx.android.synthetic.main.item_camera_info.view.*

class CameraInfoAdapter(private val listInfo: List<CameraInfo>) : RecyclerView.Adapter<CameraInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraInfoViewHolder {
        return CameraInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_camera_info, parent, false))
    }

    override fun getItemCount(): Int = listInfo.size

    override fun onBindViewHolder(holder: CameraInfoViewHolder, position: Int) {
        val cameraInfo = listInfo[position]
        holder.bind(cameraInfo)
    }
}


class CameraInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(cameraInfo: CameraInfo) = itemView.run {

        if (cameraInfo.uniqueChar != null) {
            title_info.textWithCheckEmptySetDefaultOrNol(cameraInfo.title, "--")
            value_info.textWithCheckEmptySetDefaultOrNolOrUnique(cameraInfo.value, "--", cameraInfo.uniqueChar)
        } else {
            title_info.textWithCheckEmptySetDefaultOrNol(cameraInfo.title, "--")
            value_info.textWithCheckEmptySetDefaultOrNol(cameraInfo.value, "--")
        }
    }
}