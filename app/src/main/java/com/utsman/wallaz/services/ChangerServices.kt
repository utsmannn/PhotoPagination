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
import android.app.Service
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.androidnetworking.error.ANError
import com.utsman.wallaz.data.Photos
import java.io.File

class ChangerServices : Service() {

    private var file = File(Environment.getExternalStorageDirectory(), "/.wallaz/temp")
    private var isChanger = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isChanger) {
                isChanger = false
                val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
                WallpaperManager.getInstance(context).setBitmap(wallpaper)
                Log.i("WOY", "ddddd")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

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
        }).getRandomWallpaper()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}