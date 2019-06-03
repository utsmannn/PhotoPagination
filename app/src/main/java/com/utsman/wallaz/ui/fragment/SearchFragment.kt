package com.utsman.wallaz.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.R
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.di.SearchInjector
import com.utsman.wallaz.ui.adapter.MainPagedAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.toolbar
import kotlinx.android.synthetic.main.search_fragment.*
import java.lang.Exception

class SearchFragment : Fragment() {

    private var listState: Parcelable? = null
    private lateinit var gridLayoutManager: GridLayoutManager
    private var back = false

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val searchViewModel by lazy {
        SearchInjector.injectSearchViewModel(this, context!!)
    }

    private val queryArgument by lazy {
        arguments?.getString("query_argument")
    }

    private val searchAdapter = MainPagedAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            mainActivity.onBackPressed()
            custom_search_bar.clearFocus()
        }

        val editFrame = custom_search_bar.findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)
        (editFrame.layoutParams as LinearLayout.LayoutParams).leftMargin = -24

        val gridLayoutManager = GridLayoutManager(context, 2)
        search_recycler_view.layoutManager = gridLayoutManager
        search_recycler_view.adapter = searchAdapter

        custom_search_bar.isIconified = false
        custom_search_bar.setOnCloseListener {
            mainActivity.onBackPressed()
            custom_search_bar.clearFocus()
            true
        }

        if (back) chips_group.visibility = View.GONE

        custom_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!back) {
                    searchViewModel.getPhotos(query).observe(this@SearchFragment, Observer {
                        searchAdapter.submitList(it)
                    })

                    searchViewModel.addChip(query)
                    chips_group.visibility = View.GONE
                    custom_search_bar.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                back = false
                return true
            }
        })


        if (queryArgument != null) {
            fromChipClick(queryArgument!!)
        }


        setupChips()

        mainActivity.onBackPressedDispatcher.addCallback {
            try {
                findNavController().navigateUp()
            } catch (e: Exception) {
                Handler().postDelayed({
                    findNavController().popBackStack()
                }, 1000)
            }
        }
    }

    private fun setupChips() {
        searchViewModel.getChips().observe(this, Observer { chips ->
            chips.map { chip ->
                val c = Chip(context)
                c.text = chip.query
                chips_group.addView(c)

                c.setOnLongClickListener {
                    chips_group.removeView(c)
                    searchViewModel.deleteChip(chip.query)
                    Toast.makeText(context, "Chip deleted", Toast.LENGTH_SHORT).show()
                    true
                }

                c.setOnClickListener {
                    fromChipClick(chip.query)
                }
            }
        })
    }

    private fun fromChipClick(query: String) {
        custom_search_bar.setQuery(query, true)
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