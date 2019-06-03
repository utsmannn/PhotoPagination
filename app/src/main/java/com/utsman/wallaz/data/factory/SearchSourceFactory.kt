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