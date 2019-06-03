package com.utsman.wallaz.ui.fragment

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.utsman.wallaz.*
import com.utsman.wallaz.data.NetworkState
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.di.MainInjector
import com.utsman.wallaz.di.RoomInjector
import com.utsman.wallaz.ui.CameraInfo
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.photo_bottom_sheet.*
import kotlinx.android.synthetic.main.photo_fragment.*
import java.io.File
import java.lang.Exception

class PhotoFragment : Fragment() {

    private var setupType = SetupType.DOWNLOAD
    private lateinit var photo: Photos

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val photoViewModel by lazy {
        MainInjector.injectPhotosViewModel(this)
    }

    private val roomViewModel by lazy {
        RoomInjector.injectBookmarkViewModel(this, context!!)
    }

    private val file by lazy {
        File(Environment.getExternalStorageDirectory(), "/Wallaz/${photo.id}.jpg")
    }

    private val sheetBehavior by lazy {
        BottomSheetBehavior.from(bottom_sheet_layout)
    }

    private val textShare by lazy {
        "Photo by ${photo.user.name}, get on Unsplash! \n${photo.links.html} \n\nSend from Wallaz - Amazing photos for you!"
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (setupType) {
                SetupType.WALLPAPER -> setupWallpaper(context, file)
                SetupType.SHARE -> setupShare(context, file, textShare)
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

        photo_image_view.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        drag_indicator.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED
                || sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        mainActivity.onBackPressedDispatcher.addCallback {
            when {
                sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN -> {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                else -> try {
                    findNavController().popBackStack()
                } catch (e: Exception) {
                    Handler().postDelayed({
                        findNavController().popBackStack()
                    }, 1000)
                }
            }
        }
    }

    private fun setupContent() {
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        photoViewModel.getPhotoById(arguments?.getString("photo_id")).observe(this, Observer { photo ->
            this.photo = photo

            Picasso.get().load(photo.url.regular).into(photo_image_view, object : Callback {
                override fun onSuccess() {
                    Handler().postDelayed({
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }, 800)
                }

                override fun onError(e: Exception?) {
                    progress_horizontal.visibility = View.GONE
                }

            })

            setupBottomSheetContent()
            setupBtn()
            setupCameraInfo()
            setupTag()
        })

        photoViewModel.getLoaderById().observe(this, Observer {networkState ->
            Log.i("AJIANG", networkState.msg)

            /*if (networkState == NetworkState.FAILED) error_layout.visibility = View.VISIBLE
            else error_layout.visibility = View.GONE*/

            when (networkState) {
                NetworkState.LOADED -> {
                    progress_horizontal.visibility = View.GONE
                    error_layout.visibility = View.GONE
                }
                NetworkState.LOADING -> {
                    progress_horizontal.visibility = View.VISIBLE
                    error_layout.visibility = View.GONE
                }
                NetworkState.FAILED -> {
                    progress_horizontal.visibility = View.GONE
                    error_layout.visibility = View.VISIBLE
                    bottom_sheet_layout.visibility = View.GONE
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    private fun setupTag() {

        val tags = photo.tags
        if (tags != null) {
            for (tag in tags) {
                val chip = Chip(chips_group.context)
                chip.text = tag.title

                chips_group.addView(chip)

                chip.setOnClickListener {
                    val bundle = bundleOf("query_argument" to tag.title)
                    findNavController().navigate(R.id.toSearchFragmentFromPhoto, bundle)
                }
            }
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
                    Toast.makeText(context, "bookmark", Toast.LENGTH_SHORT).show()
                } else {
                    roomViewModel.deletePhoto(photo)
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })

        btn_download.setOnClickListener {
            setupType = SetupType.DOWNLOAD
            downloadFile()
        }

        btn_set_wallpaper.setOnClickListener {
            setupType = SetupType.WALLPAPER
            downloadFile()
        }

        btn_share.setOnClickListener {
            setupType = SetupType.SHARE
            downloadFile()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun downloadFile() {
        val fileUri = Uri.fromFile(file)
        val linkUri = photo.url.raw

        val request = DownloadManager.Request(Uri.parse(linkUri))
            .setTitle("Photo by ${photo.user.name}")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(fileUri)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setVisibleInDownloadsUi(true)
        request.allowScanningByMediaScanner()

        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    @SuppressLint("SetTextI18n")
    private fun setupBottomSheetContent() {
        text_author.textWithCheckEmptyHide("Photo by ${photo.user.name}")
        text_location.textWithCheckEmptyHide(photo.location?.title)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(onDownloadComplete)
    }
}
