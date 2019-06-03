package com.utsman.wallaz.di

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.viewmodel.PhotosViewModel
import com.utsman.wallaz.viewmodel.SearchViewModel

object MainInjector {

    fun injectMainActivity(activity: Activity) = activity as MainActivity
    fun injectPhotosViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(PhotosViewModel::class.java)
    fun injectSearchViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(SearchViewModel::class.java)

}