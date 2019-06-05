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

package com.utsman.wallaz.widget

import android.app.DownloadManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.RemoteViews

import com.utsman.wallaz.R
import com.utsman.wallaz.services.ChangerServices
import java.io.File

/**
 * Implementation of App Widget functionality.
 */
@Suppress("JAVA_CLASS_ON_COMPANION")
class ChangerWidget : AppWidgetProvider() {

    private var file = File(Environment.getExternalStorageDirectory(), "/.wallaz/temp")

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
            WallpaperManager.getInstance(context).setBitmap(wallpaper)

            Log.i("BAAH", "dduh")
        }

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        context.unregisterReceiver(onDownloadComplete)
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, ChangerServices::class.java)
            intent.putExtra("changer", true)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)

            val view = RemoteViews(context.packageName, R.layout.changer_widget)
            view.setOnClickPendingIntent(R.id.click_container, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, view)

        }
    }
}

