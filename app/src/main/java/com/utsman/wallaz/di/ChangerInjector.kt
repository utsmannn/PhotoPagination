package com.utsman.wallaz.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.viewmodel.ChangerViewModel

object ChangerInjector {

    fun injectChangerViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(ChangerViewModel::class.java)
}