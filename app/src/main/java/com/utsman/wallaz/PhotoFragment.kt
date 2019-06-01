package com.utsman.wallaz

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.di.MainInjector
import kotlinx.android.synthetic.main.photo_fragment.*
import java.io.File
import java.lang.Exception

class PhotoFragment : Fragment() {

    private var expanded = false
    private var setupType = SetupType.DOWNLOAD

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val photo by lazy {
        arguments?.getParcelable("photo") as Photos
    }

    private val file by lazy {
        File(Environment.getExternalStorageDirectory(), "/.Wallaz/${photo.id}.jpg")
    }

    private val sheetBehavior by lazy {
        BottomSheetBehavior.from(bottom_sheet_layout)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (setupType) {
                SetupType.WALLPAPER -> {
                    setupWallpaper(context, file)
                }
                else -> {
                }
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



        Picasso.get().load(photo.url.regular).into(photo_image_view, object : Callback {
            override fun onSuccess() {
                progress_horizontal.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                progress_horizontal.visibility = View.GONE
            }

        })

        photo_image_view.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, offset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> expanded = false
                    BottomSheetBehavior.STATE_EXPANDED -> expanded = true
                }
            }

        })

        drag_view.setOnClickListener {
            if (expanded) sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        mainActivity.onBackPressedDispatcher.addCallback {
            if (expanded) sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else {
                try {
                    findNavController().popBackStack()
                } catch (e: Exception) {
                    findNavController().navigateUp()
                }
            }
        }

        setupBottomSheetContent()
        setupBtn()
    }

    private fun setupBtn() {
        btn_download.setOnClickListener {
            setupType = SetupType.DOWNLOAD
            downloadFile()
        }

        btn_set_wallpaper.setOnClickListener {
            setupType = SetupType.WALLPAPER
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
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
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
        text_author.text = "Photo by ${photo.user.name}"
        text_location.text = photo.user.location

        text_author.hideIfTextEmpty()
        text_location.hideIfTextEmpty()
    }

    private fun sendToIntentHelper(photoId: String) {
        val intent = Intent(context, IntentHelperActivity::class.java)
        intent.putExtra("id", photoId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(onDownloadComplete)
    }
}
