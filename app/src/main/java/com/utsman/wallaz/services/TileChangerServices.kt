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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class TileChangerServices : TileService() {

    private var isTile = false
    private lateinit var randomPhotoBuilder: RandomPhotoBuilder.Builder

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isTile) {
                isTile = false
                val closePanel = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                sendBroadcast(closePanel)
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.label = "Change Wallpaper"
                qsTile.updateTile()

                Handler().postDelayed({
                    randomPhotoBuilder.receiverDownload(context)
                }, 400)
            }
        }
    }

    override fun onTileAdded() {
        super.onTileAdded()
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        isTile = true
        Toast.makeText(applicationContext, "Changing start..", Toast.LENGTH_SHORT).show()

        qsTile.state = Tile.STATE_ACTIVE
        qsTile.label = "Changing start.."
        qsTile.updateTile()

        val sharedPref = getSharedPreferences("changer_query", Context.MODE_PRIVATE)
        val query = sharedPref.getString("query", "")

        isTile = true
        randomPhotoBuilder = RandomPhotoBuilder.Builder()
                .with(this)
                .query(query)
                .folder("/.wallaz")
                .listener(object : RandomPhotoListener {
                    override fun onLoad() {
                        Log.i("PPP", "bisa")
                    }

                    override fun onError(errorMsg: String) {
                        Log.e("PPP", errorMsg)
                        qsTile.state = Tile.STATE_INACTIVE
                        qsTile.label = "Error: $errorMsg"
                        qsTile.updateTile()
                    }
                })

        randomPhotoBuilder.build()

    }

    override fun onCreate() {
        super.onCreate()

        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.priority = 100000
        registerReceiver(onDownloadComplete, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }
}