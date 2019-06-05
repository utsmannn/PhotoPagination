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

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.data.Photos
import java.io.File


class ChangerBuilder constructor(private val context: Context,
                                 private val query: String,
                                 private val iChanger: IChanger) {

    open class Builder {

        private lateinit var context: Context
        private lateinit var query: String
        private lateinit var folder: String
        private lateinit var iChanger: IChanger
        private lateinit var file: File

        fun with(context: Context) = apply { this.context = context }
        fun query(query: String) = apply { this.query = query }
        fun folder(folder: String) = apply { this.folder = folder }
        fun listener(iChanger: IChanger) = apply { this.iChanger = iChanger }

        /**
         * Builder for services don't use threading !
         * */
        fun build(): ChangerBuilder {
            file = File(Environment.getExternalStorageDirectory(), "$folder/temp")
            AndroidNetworking.get(BuildConfig.BASE_URL)
                    .addPathParameter("endpoint", "photos/random")
                    .addQueryParameter("query", query)
                    .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                    .build()
                    .getAsObject(Photos::class.java, object : ParsedRequestListener<Photos> {
                        override fun onResponse(response: Photos) {
                            iChanger.onLoad()
                            file = File(Environment.getExternalStorageDirectory(), "$folder/${response.id}.jpg")
                            downloadFile(response.url.regular, context, file)
                        }

                        override fun onError(anError: ANError) {
                            iChanger.onError(anError.errorBody)
                        }

                    })

            return ChangerBuilder(context, query, iChanger)
        }

        private fun downloadFile(url: String, context: Context?, file: File) {
            val request = DownloadManager.Request(Uri.parse(url))
                    .setTitle("Preparing..")
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }

        fun receiverDownload(context: Context?) {
            val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
            WallpaperManager.getInstance(context).setBitmap(wallpaper)
            Log.i("WOY", "ddddd")
        }

    }

}