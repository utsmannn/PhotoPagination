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
import com.utsman.wallaz.data.DownloadLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DownloadViewModel : BaseViewModel() {

    private val downloadUrl: MutableLiveData<String> = MutableLiveData()

    fun findDownloadUrl(downloadLocation: String): LiveData<String> {
        disposable.add(
                Rx2AndroidNetworking.get(downloadLocation)
                        .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                        .build()
                        .getObjectObservable(DownloadLocation::class.java)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ download ->
                            downloadUrl.postValue(download.url)
                            Log.e("Trigger download url", download.url)
                        }, { t ->
                            Log.e("Error ${this::class.java.simpleName}: ", t.localizedMessage)
                        })

        )
        return downloadUrl
    }
}