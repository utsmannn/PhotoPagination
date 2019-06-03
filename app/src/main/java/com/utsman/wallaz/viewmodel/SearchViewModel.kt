package com.utsman.wallaz.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.wallaz.configPaged
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.factory.SearchSourceFactory

class SearchViewModel : BaseViewModel() {

    private var searchSourceFactory: SearchSourceFactory? = null

    fun getPhotos(query: String): LiveData<PagedList<Photos>> {
        searchSourceFactory = SearchSourceFactory(disposable, query)
        return LivePagedListBuilder<String, Photos>(searchSourceFactory!!, configPaged(2)).build()
    }
}