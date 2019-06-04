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

package com.utsman.wallaz.services

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.data.Photos

class ChangerHelper constructor(private val query: String?, private val iChanger: IChanger) {


    /**
     * Helper for services don't use threading !
     * */
    fun getRandomWallpaper() {
        AndroidNetworking.get(BuildConfig.BASE_URL)
            .addPathParameter("endpoint", "photos/random")
            .addQueryParameter("query", query)
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .build()
            .getAsObject(Photos::class.java, object : ParsedRequestListener<Photos> {
                override fun onResponse(response: Photos) {
                    iChanger.onLoad(response)
                }

                override fun onError(anError: ANError) {
                    iChanger.onError(anError)
                }

            })
    }

}