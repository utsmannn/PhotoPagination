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
import com.utsman.wallaz.data.paged.SearchDataSource
import io.reactivex.disposables.CompositeDisposable

class SearchSourceFactory(private val disposable: CompositeDisposable, private val query: String) : DataSource.Factory<String, Photos>() {
    val result = MutableLiveData<SearchDataSource>()
    override fun create(): DataSource<String, Photos> {
        val searchDataSource = SearchDataSource(disposable, query)
        result.postValue(searchDataSource)
        return searchDataSource
    }
}