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
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.wallaz.configPaged
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.db.PhotoRoom
import com.utsman.wallaz.db.PhotosRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BookmarkViewModel(private val photosRepository: PhotosRepository) : BaseViewModel() {
    private val millis = System.currentTimeMillis()

    fun isPhotoExists(photo: Photos): LiveData<Boolean> {
        val id = photo.id
        val photoRoom = photosRepository.checkPhoto(id)
        return Transformations.map(photoRoom) { it != null }
    }

    fun bookmarkPhoto(photo: Photos) {
        val photoRoom = PhotoRoom(photo.id, photo.url.regular, millis)
        disposable.add(
            Completable.fromAction {
                photosRepository.insertPhoto(photoRoom)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun deletePhoto(photo: Photos) {
        val photoRoom = PhotoRoom(photo.id, photo.url.regular, millis)
        disposable.add(
            Completable.fromAction {
                photosRepository.deletePhoto(photoRoom)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("anjay", "dele")
                }, {
                    Log.e("anjayyy", "gak")
                })
        )
    }

    fun getAllPagedListPhoto(): LiveData<PagedList<PhotoRoom>> {
        val factory: DataSource.Factory<Int, PhotoRoom> = photosRepository.getPagedPhoto()
        return LivePagedListBuilder<Int, PhotoRoom>(factory, configPaged(2)).build()
    }
}