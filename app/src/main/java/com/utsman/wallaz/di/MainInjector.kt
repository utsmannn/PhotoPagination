/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.wallaz.di

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.viewmodel.DownloadViewModel
import com.utsman.wallaz.viewmodel.PhotosViewModel
import com.utsman.wallaz.viewmodel.SearchViewModel

object MainInjector {

    fun injectMainActivity(activity: Activity) = activity as MainActivity
    fun injectPhotosViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(PhotosViewModel::class.java)
    fun injectDownloadPhotoViewModel(fragment: Fragment) = ViewModelProviders.of(fragment).get(DownloadViewModel::class.java)
}