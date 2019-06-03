package com.utsman.wallaz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utsman.wallaz.db.ChipHistoryRepository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val chipHistoryRepository: ChipHistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SearchViewModel(chipHistoryRepository) as T
}