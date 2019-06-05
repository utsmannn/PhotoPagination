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
import android.app.IntentService
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.R
import com.utsman.wallaz.data.Photos
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class ChangerIntent : IntentService("changer") {
    private val disposable = CompositeDisposable()

    private var file = File(Environment.getExternalStorageDirectory(), "/.wallaz")
    private var isChanger = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isChanger) {
                isChanger = false
                val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
                WallpaperManager.getInstance(context).setBitmap(wallpaper)
            }
            Log.i("WOY", "ddddd")
        }

    }

    override fun onHandleIntent(intent: Intent?) {
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        //application.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        /*Log.i("BANGGGGG", "aaaaaaaaa")
        ChangerHelper("city", object : IChanger {
            override fun onLoad(photos: Photos) {
                isChanger = true
                file = File(Environment.getExternalStorageDirectory(), "/.wallaz/${photos.id}.jpg")
                val request = DownloadManager.Request(Uri.parse(photos.url.regular))
                        .setTitle("Preparing..")
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
                Log.i("BANGKEEEEE", "dwonload start")
            }

            override fun onError(anError: ANError) {
                Log.e("error", anError.errorBody)
            }
        }).getRandomWallpaper()*/
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.i("baaah", "dddd")
    }

    override fun onCreate() {
        super.onCreate()
        //application.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        super.onDestroy()
        //application.unregisterReceiver(receiver)
        disposable.clear()
    }
}