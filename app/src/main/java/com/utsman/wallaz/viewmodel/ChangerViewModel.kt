package com.utsman.wallaz.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.data.Photos
import io.reactivex.schedulers.Schedulers

class ChangerViewModel : BaseViewModel() {

    private val photo: MutableLiveData<Photos> = MutableLiveData()

    private fun fetchPhotoRandom() = Rx2AndroidNetworking.get(BuildConfig.BASE_URL)
            .addPathParameter("endpoint", "photos/random?query=city")
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .build()
            .getObjectObservable(Photos::class.java)

    fun getPhotoRandom(): LiveData<Photos> {
        disposable.add(
                fetchPhotoRandom()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .subscribe({ photo ->
                            this.photo.postValue(photo)
                        }, { t ->
                            Log.e("ANJAY", t.localizedMessage)
                        })
        )

        return photo
    }


    // private fun fetchPhotoById(id: String?) = Rx2AndroidNetworking.get(BuildConfig.BASE_URL)
    //            .addPathParameter("endpoint", "photos/$id")
    //            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
    //            .build()
    //            .getObjectObservable(Photos::class.java)
    //
    //    fun getPhotoById(id: String?): LiveData<Photos> {
    //        loader.postValue(NetworkState.LOADING)
    //        Log.i("BANGKEEE", id.toString())
    //        disposable.add(
    //                fetchPhotoById(id)
    //                        .subscribeOn(Schedulers.io())
    //                        .observeOn(AndroidSchedulers.mainThread())
    //                        .subscribe({ photo ->
    //                            this.photo.postValue(photo)
    //                            Log.i("ANJAYLAHHHH", photo.url.full)
    //                            loader.postValue(NetworkState.LOADED)
    //
    //                        }, {
    //                            loader.postValue(NetworkState.FAILED)
    //                        })
    //        )
    //
    //        return photo
    //    }
}