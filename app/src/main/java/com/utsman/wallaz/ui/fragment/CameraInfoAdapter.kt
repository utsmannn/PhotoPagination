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