package com.utsman.wallaz.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.R
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.ui.adapter.MainPagedAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.toolbar
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private var listState: Parcelable? = null
    private lateinit var gridLayoutManager: GridLayoutManager
    private var back = false

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val searchViewModel by lazy {
        MainInjector.injectSearchViewModel(this)
    }

    private val searchAdapter = MainPagedAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_back)

        val editFrame = custom_search_bar.findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)
        (editFrame.layoutParams as LinearLayout.LayoutParams).leftMargin = -24
        custom_search_bar.isIconified = false

        val gridLayoutManager = GridLayoutManager(context, 2)
        search_recycler_view.layoutManager = gridLayoutManager
        search_recycler_view.adapter = searchAdapter

            custom_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    back = false
                    if (!back) {
                        searchViewModel.getPhotos(newText).observe(this@SearchFragment, Observer {
                            searchAdapter.submitList(it)
                        })
                    }
                    return true
                }
            })

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