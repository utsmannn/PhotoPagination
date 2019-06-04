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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.utsman.wallaz.BuildConfig
import com.utsman.wallaz.data.Photos
import java.io.File

@RequiresApi(Build.VERSION_CODES.N)
class TileChangerServices : TileService() {

    private var file = File(Environment.getExternalStorageDirectory(), "/.wallaz/temp")
    private var isTile = false

    private val onDownloadComplete  = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.label = "Change Wallpaper"
            qsTile.updateTile()

            if (isTile) {
                val closePanel = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                sendBroadcast(closePanel)

                Handler().postDelayed({
                    val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
                    WallpaperManager.getInstance(context).setBitmap(wallpaper)
                }, 500)

                isTile = false
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

        ChangerHelper(query, object : IChanger {
            override fun onLoad(photos: Photos) {
                Log.i("BANGEEE", photos.url.regular)
                file = File(Environment.getExternalStorageDirectory(), "/.wallaz/${photos.id}.jpg")
                downloadFile(photos.url.regular)
            }

            override fun onError(anError: ANError) {
                Log.e("BANKE", anError.errorBody)
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.label = "Error: ${anError.errorDetail}"
                qsTile.updateTile()
            }
        }).getRandomWallpaper()
    }

    private fun downloadFile(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Preparing..")
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
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