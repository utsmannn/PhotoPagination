package com.utsman.wallaz.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.wallaz.R
import com.utsman.wallaz.data.Tag
import kotlinx.android.synthetic.main.item_tag.view.*

class TagAdapter(private val list: List<Tag>) : RecyclerView.Adapter<TagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = list[position]
        holder.bind(tag)
    }
}

class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(tag: Tag) = itemView.run {
        text_tag.text = tag.title
    }
}