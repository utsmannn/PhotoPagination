package com.utsman.wallaz.data.paged

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PhotosDataSource(private val disposable: CompositeDisposable, private val orderBy: String) : ItemKeyedDataSource<String, Photos>() {

    private var nextPage: Long = 1
    val networkState = MutableLiveData<NetworkState>()

    private fun rxNetwork(page: Long) = Rx2AndroidNetworking.get(BuildConfig.BASE_URL)
            .addPathParameter("endpoint", "photos")
            .addQueryParameter("page", page.toString())
            .addQueryParameter("order_by", orderBy)
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .build()
            .getObjectListObservable(Photos::class.java)

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Photos>) {
        networkState.postValue(NetworkState.LOADING)
        disposable.add(
                rxNetwork(nextPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            nextPage++
                        }
                        .subscribe({ photos ->
                            callback.onResult(photos)
                            networkState.postValue(NetworkState.LOADED)
                        }, { t ->
                            try {
                                Log.e("ANJAY", t.localizedMessage)
                            } catch (e: Exception) {
                                Log.e("ANJAY", e.localizedMessage)
                            }

                            networkState.postValue(NetworkState.LOADED)

                        })
        )
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Photos>) {
        networkState.postValue(NetworkState.LOADING)
        disposable.add(
                rxNetwork(nextPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            nextPage++
                        }
                        .delay(600, TimeUnit.MILLISECONDS)
                        .subscribe({ photos ->
                            callback.onResult(photos)
                            networkState.postValue(NetworkState.LOADED)
                        }, { t ->
                            try {
                                Log.e("ANJAY", t.localizedMessage)
                            } catch (e: Exception) {
                                Log.e("ANJAY", e.localizedMessage)
                            }

                            networkState.postValue(NetworkState.LOADED)

                        })
        )
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Photos>) {
    }

    override fun getKey(item: Photos): String = item.id
}