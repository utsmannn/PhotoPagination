package com.utsman.wallaz

import android.view.View
import android.widget.TextView
import androidx.paging.PagedList

fun configPaged(size: Int): PagedList.Config = PagedList.Config.Builder()
    .setPageSize(size)
    .setInitialLoadSizeHint(size)
    .setEnablePlaceholders(true)
    .build()

fun TextView.hideIfTextEmpty() {
    visibility = if (text.isNullOrEmpty()) View.GONE
    else View.VISIBLE
}