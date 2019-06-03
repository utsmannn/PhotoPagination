package com.utsman.wallaz.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.paged.PhotosDataSource
import io.reactivex.disposables.CompositeDisposable

class PhotosSourceFactory(private val disposable: CompositeDisposable, private val orderBy: String): DataSource.Factory<String, Photos>() {

    val photos = MutableLiveData<PhotosDataSource>()
    override fun create(): DataSource<String, Photos> {
        val photosDataSource = PhotosDataSource(disposable, orderBy)
        photos.postValue(photosDataSource)
        return photosDataSource
    }
}