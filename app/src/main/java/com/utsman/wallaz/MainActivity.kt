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

package com.utsman.wallaz

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.service.quicksettings.Tile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.androidnetworking.error.ANError
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.services.ChangerHelper
import com.utsman.wallaz.services.IChanger
import java.io.File

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST = 1
    private var file = File(Environment.getExternalStorageDirectory(), "/.wallaz/temp")
    private lateinit var drawer: Drawer
    private var isChanger = false

    private val onDownloadComplete  = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (isChanger) {
                isChanger = false
                val wallpaper = BitmapFactory.decodeFile(file.absolutePath)
                WallpaperManager.getInstance(context).setBitmap(wallpaper)
                Log.i("WOY", "ddddd")
                finish()
                overridePendingTransition(R.anim.nav_default_exit_anim, R.anim.nav_default_exit_anim)
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val changer = intent.getBooleanExtra("changer", false)
        if (changer) {
            initChanger()
        } else {
            initView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    private fun initChanger() {
        setTheme(R.style.AppTheme_Translucent)
        isChanger = true
        val sharedPref = getSharedPreferences("changer_query", Context.MODE_PRIVATE)
        val query = sharedPref.getString("query", "")
        ChangerHelper(query, object : IChanger {
            override fun onLoad(photos: Photos) {
                file = File(Environment.getExternalStorageDirectory(), "/.wallaz/${photos.id}.jpg")
                val request = DownloadManager.Request(Uri.parse(photos.url.regular))
                        .setTitle("Preparing..")
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
                Toast.makeText(this@MainActivity, "Changing start...", Toast.LENGTH_SHORT).show()
                Log.i("BANGKEEEEE", "dddddd")
            }

            override fun onError(anError: ANError) {
                Log.e("BANGKEE", anError.errorBody)
                Toast.makeText(this@MainActivity, "Changing error!", Toast.LENGTH_SHORT).show()
                finish()
                overridePendingTransition(R.anim.nav_default_exit_anim, R.anim.nav_default_exit_anim)
            }

        }).getRandomWallpaper()
    }

    private fun initView() {
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setupWindow()
        setupPermission()

        val item1 = itemDrawer().withIdentifier(1)
            .withName("All Photos")
            .withOnDrawerItemClickListener { view, position, drawerItem ->
                val bundle = bundleOf("order_by" to "latest")
                findNavController(R.id.main_host_fragment_app).navigate(R.id.toMainFragment, bundle)
                false
            }

        val item2 = itemDrawer().withIdentifier(2)
            .withName("Popular Photos")
            .withOnDrawerItemClickListener { view, position, drawerItem ->
                val bundle = bundleOf("order_by" to "popular")
                findNavController(R.id.main_host_fragment_app).navigate(R.id.toMainFragment, bundle)
                false
            }

        val item3 = itemDrawer().withIdentifier(3)
            .withName("Collections")

        val item4 = itemDrawer().withIdentifier(4)
            .withName("Bookmark")
            .withOnDrawerItemClickListener { view, position, drawerItem ->
                findNavController(R.id.main_host_fragment_app).navigate(R.id.toBookmarkFragment)
                false
            }
            .withSelectable(false)

        val item5 = itemDrawer().withIdentifier(5)
            .withName("One Click Change")
            .withOnDrawerItemClickListener { view, position, drawerItem ->
                //findNavController(R.id.main_host_fragment_app).navigate(R.id.toOneClickChangerFragment)
                false
            }

        drawer = DrawerBuilder()
            .withActivity(this)
            .addDrawerItems(item1, item2, item3, DividerDrawerItem(), item4, item5)
            .build()

        val view = LayoutInflater.from(this).inflate(R.layout.header_drawer, null)
        drawer.setHeader(view, false, true, DimenHolder.fromDp(200))
    }

    private fun setupWindow() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun itemDrawer(): PrimaryDrawerItem = PrimaryDrawerItem()
        .withIconTintingEnabled(true)
        .withIconColorRes(R.color.colorPrimary)
        .withTextColorRes(R.color.colorPrimary)
        .withSelectedTextColorRes(R.color.colorPrimary)
        .withSelectedColorRes(R.color.tintMenu)
        .withSelectedBackgroundAnimated(true)

    fun openDrawer() = drawer.openDrawer()
    fun isDrawerOpen(): Boolean = drawer.isDrawerOpen
    fun closeDrawer() = drawer.closeDrawer()
    fun toSearchFragment() = findNavController(R.id.main_host_fragment_app).navigate(R.id.toSearchFragment)

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.main_host_fragment_app).navigateUp()
    }

    private fun setupPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
        } else {
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }

            else -> Toast.makeText(this, "ignore", Toast.LENGTH_SHORT).show()
        }
    }
}