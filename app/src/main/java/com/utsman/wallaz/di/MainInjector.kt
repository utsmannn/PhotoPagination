package com.utsman.wallaz.di

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.ui.MainPagedAdapter
import com.utsman.wallaz.ui.PhotosViewModel

object MainInjector {

    fun injectMainActivity(activity: Activity) = activity as MainActivity
    fun injectPhotosViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(PhotosViewModel::class.java)
}