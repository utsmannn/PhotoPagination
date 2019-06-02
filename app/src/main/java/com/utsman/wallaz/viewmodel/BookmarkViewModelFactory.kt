package com.utsman.wallaz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utsman.wallaz.db.PhotosRepository

@Suppress("UNCHECKED_CAST")
class BookmarkViewModelFactory(private val photosRepository: PhotosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = BookmarkViewModel(photosRepository) as T
}