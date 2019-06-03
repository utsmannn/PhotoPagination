package com.utsman.wallaz.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.R
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.di.RoomInjector
import com.utsman.wallaz.ui.adapter.BookmarkPagedAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class BookmarkFragment : Fragment() {

    private lateinit var gridLayoutManager: GridLayoutManager
    private var back = false
    private val bookmarkAdapter = BookmarkPagedAdapter()
    private var listState: Parcelable? = null

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val bookmarkViewModel by lazy {
        RoomInjector.injectBookmarkViewModel(this, context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Bookmark"

        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_nav)
        toolbar.setNavigationOnClickListener {
            mainActivity.openDrawer()
        }

        if (!back) fetchData()

        swipe_layout.setProgressViewEndTarget(true, 400)
        swipe_layout.setOnRefreshListener {
            fetchData()
        }

        gridLayoutManager = GridLayoutManager(context, 2)

        main_recycler_view.layoutManager = gridLayoutManager
        main_recycler_view.adapter = bookmarkAdapter

        mainActivity.onBackPressedDispatcher.addCallback {
            if (mainActivity.isDrawerOpen()) mainActivity.closeDrawer()
            else mainActivity.finish()
        }

        mainActivity.onBackPressedDispatcher.addCallback {
            try {
                findNavController().popBackStack()
            } catch (e: Exception) {
                Handler().postDelayed({
                    findNavController().popBackStack()
                }, 1000)
            }
        }

    }

    private fun fetchData() {
        bookmarkViewModel.getAllPagedListPhoto().observe(this, Observer {photoRooms ->
            bookmarkAdapter.submitList(photoRooms)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (main_recycler_view != null) {
            listState = main_recycler_view.layoutManager?.onSaveInstanceState()
            outState.putParcelable("save_main_recycler_view", listState)
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("save_main_recycler_view")
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