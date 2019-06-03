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

    private val onDownloadComplete  = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
            WallpaperManager.getInstance(context).setBitmap(wallpaper)
        }
    }

    override fun onTileAdded() {
        super.onTileAdded()
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        Toast.makeText(applicationContext, "Changing start..", Toast.LENGTH_SHORT).show()

        val sharedPref = getSharedPreferences("changer_query", Context.MODE_PRIVATE)
        val query = sharedPref.getString("query", "")

        val closePanel = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        sendBroadcast(closePanel)

        /**
         * Cannot make threading in Tile Services !
         * */
        AndroidNetworking.get(BuildConfig.BASE_URL)
                .addPathParameter("endpoint", "photos/random")
                .addQueryParameter("query", query)
                .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                .build()
                .getAsObject(Photos::class.java, object : ParsedRequestListener<Photos> {
                    override fun onResponse(response: Photos) {
                        Log.i("BANGEEE", response.url.regular)
                        file = File(Environment.getExternalStorageDirectory(), "/.wallaz/${response.id}.jpg")
                        downloadFile(response.url.regular)
                    }

                    override fun onError(anError: ANError?) {
                        Log.e("DDDD", anError?.errorBody)
                    }

                })
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
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }
}