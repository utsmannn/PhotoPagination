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