/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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