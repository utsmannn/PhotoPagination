package com.utsman.wallaz.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.db.ChipHistoryDatabase
import com.utsman.wallaz.db.ChipHistoryRepository
import com.utsman.wallaz.viewmodel.SearchViewModel
import com.utsman.wallaz.viewmodel.SearchViewModelFactory

object SearchInjector {

    private fun getChipRepo(context: Context): ChipHistoryRepository = ChipHistoryRepository.getInstance(ChipHistoryDatabase.getInstance(context).chipHistoryDao())
    private fun proviceChipsViewModel(context: Context): SearchViewModelFactory = SearchViewModelFactory(getChipRepo(context))
    fun injectSearchViewModel(fragment: Fragment, context: Context) = ViewModelProviders.of(fragment, proviceChipsViewModel(context)).get(SearchViewModel::class.java)

}