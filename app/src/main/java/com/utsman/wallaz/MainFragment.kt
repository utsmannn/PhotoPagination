package com.utsman.wallaz

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.ui.MainPagedAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private var listState: Parcelable? = null
    private lateinit var gridLayoutManager: GridLayoutManager

    private var back = false

    private val order by lazy {
        arguments?.getString("order_by")
    }

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val photoViewModel by lazy {
        MainInjector.injectPhotosViewModel(this)
    }

    private val photoPagedAdapter = MainPagedAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager = GridLayoutManager(context, 2)

        toolbar.title = "All Photos"

        when (order) {
            "latest" -> toolbar.title = "All Photos"
            "popular" -> toolbar.title = "Popular Photos"
        }

        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_nav)
        toolbar.setNavigationOnClickListener {
            mainActivity.openDrawer()
        }

        if (!back) fetchData()

        swipe_layout.setProgressViewEndTarget(true, 400)
        swipe_layout.setOnRefreshListener {
            fetchData()
        }

        main_recycler_view.layoutManager = gridLayoutManager
        main_recycler_view.adapter = photoPagedAdapter

        /*mainActivity.onBackPressedDispatcher.addCallback {
            mainActivity.finish()
        }*/
    }

    private fun fetchData() {
        if (order != null) {
            photoViewModel.getPhotos(order!!).observe(this, Observer { photos ->
                photoPagedAdapter.submitList(photos)
            })
        } else {
            photoViewModel.getPhotos("latest").observe(this, Observer { photos ->
                photoPagedAdapter.submitList(photos)
            })
        }
        photoViewModel.getLoader().observe(this, Observer {networkState ->
            swipe_layout.isRefreshing = networkState == NetworkState.LOADING
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (main_recycler_view != null) {
            listState = main_recycler_view.layoutManager?.onSaveInstanceState()
            outState.putParcelable("save_main_recycler_view_$order", listState)
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("save_main_recycler_view_$order")
        }
    }

    override fun onResume() {
        super.onResume()
        if (listState != null) {
            gridLayoutManager.onRestoreInstanceState(listState)
            Log.i("ANJAY", "ada")
        } else {
            Log.i("ANJAY", "gk ada")
        }
    }

    override fun onPause() {
        super.onPause()
        back = true
    }
}