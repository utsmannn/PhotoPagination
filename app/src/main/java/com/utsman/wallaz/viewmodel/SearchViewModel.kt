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
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.wallaz.configPaged
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.factory.SearchSourceFactory
import com.utsman.wallaz.data.paged.SearchDataSource
import com.utsman.wallaz.db.ChipHistoryRepository
import com.utsman.wallaz.db.ChipHistoryRoom
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val chipHistoryRepository: ChipHistoryRepository) : BaseViewModel() {

    private var searchSourceFactory: SearchSourceFactory? = null
    private val milis = System.currentTimeMillis()
    private val chips: MutableLiveData<List<ChipHistoryRoom>> = MutableLiveData()

    init {
        disposable.add(
            Completable.fromAction {
                chips.postValue(chipHistoryRepository.getAllChips())
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun getPhotos(query: String): LiveData<PagedList<Photos>> {
        searchSourceFactory = SearchSourceFactory(disposable, query)
        return LivePagedListBuilder<String, Photos>(searchSourceFactory!!, configPaged(2)).build()
    }

    fun getChips(): MutableLiveData<List<ChipHistoryRoom>> {
        return chips
    }

    fun addChip(query: String) {
        val chipHistoryRoom = ChipHistoryRoom(milis, query)
        disposable.add(
            Completable.fromAction {
                chipHistoryRepository.insertChip(chipHistoryRoom)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun deleteChip(query: String) {
        val chipHistoryRoom = ChipHistoryRoom(milis, query)
        disposable.add(
            Completable.fromAction {
                chipHistoryRepository.deleteChip(chipHistoryRoom)
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


    fun isChipExists(query: String): LiveData<Boolean> {
        val chipHistoryRoom = chipHistoryRepository.getChip(query)
        return Transformations.map(chipHistoryRoom) { it != null }
    }
}