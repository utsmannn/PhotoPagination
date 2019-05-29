package com.utsman.wallaz.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.wallaz.configPaged
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.PhotosDataSource
import com.utsman.wallaz.data.PhotosSourceFactory

class PhotosViewModel : BaseViewModel() {

    private var photosSourceFactory: PhotosSourceFactory? = null

    fun getPhotos(orderBy: String): LiveData<PagedList<Photos>> {
        photosSourceFactory = PhotosSourceFactory(disposable, orderBy)
        return LivePagedListBuilder<String, Photos>(photosSourceFactory!!, configPaged(2)).build()
    }

    fun getLoader(): LiveData<NetworkState> = Transformations.switchMap<PhotosDataSource, NetworkState>(
        photosSourceFactory?.photos!!
    ) { it.networkState }
}