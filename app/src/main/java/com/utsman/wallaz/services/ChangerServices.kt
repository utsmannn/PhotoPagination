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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.utsman.wallaz.builder.RandomPhotoBuilder
import com.utsman.wallaz.builder.RandomPhotoListener

class ChangerServices : Service() {

    private var isChanger = false
    private lateinit var randomPhotoBuilder: RandomPhotoBuilder.Builder

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isChanger) {
                randomPhotoBuilder.receiverDownload(context)
                isChanger = false
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isChanger = true
        randomPhotoBuilder = RandomPhotoBuilder.Builder()
                .with(this)
                .query("city")
                .folder("/.wallaz")
                .listener(object : RandomPhotoListener {
                    override fun onLoad() {
                        Log.i("WAAH", "sukses")
                        Toast.makeText(this@ChangerServices, "Changing start..", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(errorMsg: String) {
                        Log.e("WAAH", errorMsg)
                        Toast.makeText(this@ChangerServices, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                    }

                })

        randomPhotoBuilder.build()

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