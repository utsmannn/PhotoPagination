package com.utsman.wallaz.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.configPaged
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.paged.PhotosDataSource
import com.utsman.wallaz.data.factory.PhotosSourceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PhotosViewModel : BaseViewModel() {

    private var photosSourceFactory: PhotosSourceFactory? = null
    private val photo: MutableLiveData<Photos> = MutableLiveData()
    private val loader: MutableLiveData<NetworkState> = MutableLiveData()


    fun getPhotos(orderBy: String): LiveData<PagedList<Photos>> {
        photosSourceFactory = PhotosSourceFactory(disposable, orderBy)
        return LivePagedListBuilder<String, Photos>(photosSourceFactory!!, configPaged(2)).build()
    }

    fun getLoader(): LiveData<NetworkState> = Transformations.switchMap<PhotosDataSource, NetworkState>(
        photosSourceFactory?.photos!!
    ) { it.networkState }

    private fun fetchPhotoById(id: String?) = Rx2AndroidNetworking.get(BuildConfig.BASE_URL)
            .addPathParameter("endpoint", "photos/$id")
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .build()
            .getObjectObservable(Photos::class.java)

    fun getPhotoById(id: String?): LiveData<Photos> {
        loader.postValue(NetworkState.LOADING)
        Log.i("BANGKEEE", id.toString())
        disposable.add(
                fetchPhotoById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ photo ->
                            this.photo.postValue(photo)
                            Log.i("ANJAYLAHHHH", photo.url.full)
                            loader.postValue(NetworkState.LOADED)

                        }, {
                            loader.postValue(NetworkState.FAILED)
                        })
        )

        return photo
    }

    fun getLoaderById(): LiveData<NetworkState> = loader
}