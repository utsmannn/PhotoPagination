package com.utsman.wallaz.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.db.PhotoDatabase
import com.utsman.wallaz.db.PhotosRepository
import com.utsman.wallaz.viewmodel.BookmarkViewModel
import com.utsman.wallaz.viewmodel.BookmarkViewModelFactory

object RoomInjector {

    private fun getBookmarkRepo(context: Context): PhotosRepository = PhotosRepository.getInstance(PhotoDatabase.getInstance(context).photoDao())
    private fun provideBookmarkListViewModel(context: Context): BookmarkViewModelFactory = BookmarkViewModelFactory(getBookmarkRepo(context))

    fun injectBookmarkViewModel(fragment: Fragment, context: Context) = ViewModelProviders.of(fragment, provideBookmarkListViewModel(context)).get(BookmarkViewModel::class.java)

    //fun provideListBookmarkViewModel(context: Context)

    //     fun providePlantListViewModelFactory(context: Context): PlantListViewModelFactory {
    //        val repository = getPlantRepository(context)
    //        return PlantListViewModelFactory(repository)
    //    }
}