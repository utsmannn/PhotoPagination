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

package com.utsman.wallaz.ui.fragment

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.utsman.wallaz.*
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.data.Tag
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.di.RoomInjector
import com.utsman.wallaz.ui.CameraInfo
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.photo_fragment.*
import java.io.File
import java.lang.Exception
import java.lang.IllegalStateException


/**
 * Injector always using lazy method
 * */
class PhotoFragment : Fragment() {

    private var setupType = SetupType.DOWNLOAD
    private lateinit var photo: Photos

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val photoViewModel by lazy {
        MainInjector.injectPhotosViewModel(this)
    }

    private val downloadViewModel by lazy {
        MainInjector.injectDownloadPhotoViewModel(this)
    }

    private val roomViewModel by lazy {
        RoomInjector.injectBookmarkViewModel(this, context!!)
    }

    private val file by lazy {
        File(Environment.getExternalStorageDirectory(), "/Wallaz/${photo.id}.jpg")
    }

    private val fileTemp by lazy {
        File(Environment.getExternalStorageDirectory(), "/.wallaz/${photo.id}.jpg")
    }

    private val linkWallpaper by lazy {
        photo.url.regular
    }

    private val linkTemp by lazy {
        photo.url.small
    }

    private val linkDownload by lazy {
        photo.links.downloadLocation
    }

    private val textShare by lazy {
        "Photo by ${photo.user.name}, get on Unsplash! \n${photo.links.html} \n\nSend from Wallaz - Amazing photos for you!"
    }


    /**
     * BroadcastReceiver for receiving download complete
     * */
    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (setupType) {
                SetupType.WALLPAPER -> setupWallpaper(context, fileTemp)
                SetupType.SHARE -> setupShare(context, fileTemp, textShare)
                else -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        context?.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photo_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
        mainActivity.onBackPressedDispatcher.addCallback {
            backListener()
        }
    }


    /**
     * I don't understand different of popBackStack and navigateUp :p
     * */
    private fun backListener() {
        try {
            findNavController().popBackStack()
        } catch (e: Exception) {
            Handler().postDelayed({
                findNavController().popBackStack()
            }, 1000)
        }
    }


    private fun setupContent() {
        toolbar.inflateMenu(R.menu.photo_menu)
        info_container.visibility = View.GONE
        val photoId = arguments?.getString("photo_id")

        /**
         * using list chip to get constant size of chips
         * */
        val listChip: MutableList<Chip> = mutableListOf()


        /**
         * View Model always recall after back up
         * */
        photoViewModel.getPhotoById(photoId).observe(this, Observer { photo ->
            this.photo = photo

            context?.let { ctx ->
                Glide.with(ctx)
                        .load(photo.url.small)
                        .onlyRetrieveFromCache(true)
                        .into(photo_image_view)
            }

            photo_image_view.setOnClickListener {
                val bundle = bundleOf("url" to photo.url.full, "color" to photo.color)
                findNavController().navigate(R.id.toPhotoFullFragment, bundle)
            }

            setupMetaPhoto()
            setupBtn()
            setupCameraInfo()
            setupToolbar()
        })

        photoViewModel.getLoaderById().observe(this, Observer {networkState ->
            Log.i("AJIANG", networkState.msg)

            when (networkState) {
                NetworkState.LOADED -> {
                    info_container.visibility = View.VISIBLE
                    progress_horizontal.visibility = View.GONE
                    error_layout.visibility = View.GONE
                }
                NetworkState.LOADING -> {
                    info_container.visibility = View.GONE
                    progress_horizontal.visibility = View.VISIBLE
                    error_layout.visibility = View.GONE
                }
                NetworkState.FAILED -> {
                    info_container.visibility = View.GONE
                    progress_horizontal.visibility = View.GONE
                    error_layout.visibility = View.VISIBLE
                }
            }
        })


        photoViewModel.getTags().observe(this, Observer { tags ->
            tags.map { tag ->
                val chip = Chip(chips_group.context)
                chip.text = tag.title
                listChip.add(chip)

                chip.setOnClickListener {
                    val bundle = bundleOf("query_argument" to tag.title)
                    findNavController().navigate(R.id.toSearchFragmentFromPhoto, bundle)
                }
            }


            /**
             * To prevent error add view because I dont need remove view
             * for each calling view model
             * */
            try {
                listChip.map { c ->
                    chips_group.addView(c)
                }
            } catch (e: IllegalStateException) {}

        })
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            backListener()
        }
        val menuLink = toolbar.menu.findItem(R.id.action_link)
        menuLink.setOnMenuItemClickListener {
            customTab(context).launchUrl(context, Uri.parse(photo.links.html))
            Log.i("Link photo ${this::class.java.simpleName}:", photo.links.html)
            true
        }
    }

    private fun setupCameraInfo() {
        val cameraInfos: MutableList<CameraInfo> = mutableListOf()
        cameraInfos.apply {
            add(CameraInfo("Camera Make", photo.exif?.make))
            add(CameraInfo("Camera Model", photo.exif?.model))
            add(CameraInfo("Focal Length", photo.exif?.focalLength))
            add(CameraInfo("Aperture", photo.exif?.aperture, "ƒ/"))
            add(CameraInfo("Shutter Speed", photo.exif?.exposureTime))
            add(CameraInfo("ISO", photo.exif?.iso.toString()))
            add(CameraInfo("Dimension", "${photo.w} x ${photo.h}"))
        }

        camera_info_recycler_view.layoutManager = GridLayoutManager(context, 3)
        camera_info_recycler_view.adapter = CameraInfoAdapter(cameraInfos)
    }

    private fun setupBtn() {
        roomViewModel.isPhotoExists(photo).observe(this, Observer { exists ->
            if (!exists) {
                btn_bookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_border))
            } else {
                btn_bookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark))
            }

            btn_bookmark.setOnClickListener {
                if (!exists) {
                    roomViewModel.bookmarkPhoto(photo)
                    Toast.makeText(context, "bookmarked", Toast.LENGTH_SHORT).show()
                } else {
                    roomViewModel.deletePhoto(photo)
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })

        btn_download.setOnClickListener {
            setupType = SetupType.DOWNLOAD
            downloadFile()
            Toast.makeText(context, "Download stated", Toast.LENGTH_SHORT).show()
        }

        btn_set_wallpaper.setOnClickListener {
            setupType = SetupType.WALLPAPER
            downloadFile()
            Toast.makeText(context, "Preparing", Toast.LENGTH_SHORT).show()
        }

        btn_share.setOnClickListener {
            setupType = SetupType.SHARE
            downloadFile()
            Toast.makeText(context, "Preparing", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun downloadFile() {
        when (setupType) {
            SetupType.DOWNLOAD -> downloadViewModel.findDownloadUrl(linkDownload).observe(this, Observer { url ->
                val request = DownloadManager.Request(Uri.parse(url))
                        .setTitle("Photo by ${photo.user.name} on Unsplash")
                        .setDescription("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                        .setVisibleInDownloadsUi(true)
                request.allowScanningByMediaScanner()

                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)

            })
            SetupType.WALLPAPER -> {
                val request = DownloadManager.Request(Uri.parse(linkWallpaper))
                        .setTitle("Photo by ${photo.user.name} on Unsplash")
                        .setDescription("Downloading")
                        .setDestinationUri(Uri.fromFile(fileTemp))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                request.allowScanningByMediaScanner()

                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            }
            SetupType.SHARE -> {
                val request = DownloadManager.Request(Uri.parse(linkTemp))
                        .setTitle("Photo by ${photo.user.name} on Unsplash")
                        .setDescription("Downloading")
                        .setDestinationUri(Uri.fromFile(fileTemp))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                request.allowScanningByMediaScanner()

                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupMetaPhoto() {
        val appName = "wallaz"
        val attrUser = "https://unsplash.com/@${photo.user.username}?utm_source=$appName&utm_medium=referral"
        val attrUnsplash = "https://unsplash.com/?utm_source=$appName&utm_medium=referral"

        text_author.text = photo.user.name
        text_author.setOnClickListener {
            customTab(context).launchUrl(context, Uri.parse(attrUser))
            Log.i("attrUser", attrUser)
        }
        text_unsplash.setOnClickListener {
            customTab(context).launchUrl(context, Uri.parse(attrUnsplash))
            Log.i("attrUnsplash", attrUnsplash)
        }


        text_location.textWithCheckEmptyHide(photo.location?.title)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(onDownloadComplete)
    }
}
