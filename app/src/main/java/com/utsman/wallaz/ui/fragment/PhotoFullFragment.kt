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

package com.utsman.wallaz.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.utsman.wallaz.MainActivity
import com.utsman.wallaz.R
import com.utsman.wallaz.di.MainInjector
import kotlinx.android.synthetic.main.photo_full_fragment.*
import java.lang.Exception

class PhotoFullFragment : Fragment() {

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photo_full_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")
        val colorString = arguments?.getString("color")

        context?.let { ctx ->
            Glide.with(ctx)
                    .load(url)
                    .into(photo_full_view)
                    .waitForLayout()
        }
    }


    private fun backListener() {
        try {
            findNavController().navigateUp()
        } catch (e: Exception) {
            Handler().postDelayed({
                findNavController().navigateUp()
            }, 1000)
        }
    }
}