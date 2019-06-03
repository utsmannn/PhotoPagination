package com.utsman.wallaz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

fun configPaged(size: Int): PagedList.Config = PagedList.Config.Builder()
    .setPageSize(size)
    .setInitialLoadSizeHint(size)
    .setEnablePlaceholders(true)
    .build()

fun TextView.hideIfTextEmpty() {
    visibility = if (text.isNullOrEmpty()) View.GONE
    else View.VISIBLE
}

fun TextView.textWithCheckEmptyHide(content: String?) {
    if (content.isNullOrEmpty()) visibility = View.GONE
    else text = content
}

fun TextView.textWithCheckEmptySetDefaultOrNol(content: String?, default: String) {
    text = when {
        content.isNullOrEmpty() -> default
        content == "0" -> default
        else -> content
    }
}

fun TextView.textWithCheckEmptySetDefaultOrNolOrUnique(content: String?, default: String, uniqueChar: String?) {
    text = when {
        content.isNullOrEmpty() && !uniqueChar.isNullOrEmpty() -> default
        content == "0" -> default
        else -> "$uniqueChar$content"
    }
}

fun getFileUri(context: Context, file: File): Uri {
    return FileProvider.getUriForFile(context,
        "com.utsman.wallaz.fileprovider", file)
}

fun setupWallpaper(context: Context?, file: File) {
    if (context != null) {
        val uri = getFileUri(context, file)
        try {
            val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                setDataAndType(uri, "image/*")
                putExtra("mimeType", "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Set as"))
            Log.i("ANJAY", "sukses")
        } catch (e: Exception) {
            Log.e("ANJAY", e.localizedMessage.toString())
        }
    }
}

fun setupShare(context: Context?, file: File, text: String) {
    if (context != null) {
        val uri = getFileUri(context, file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setDataAndType(uri, context.contentResolver.getType(uri))
            putExtra(Intent.EXTRA_TEXT, text)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Choose app"))
    }
}

enum class SetupType {
    WALLPAPER,
    DOWNLOAD,
    SHARE
}