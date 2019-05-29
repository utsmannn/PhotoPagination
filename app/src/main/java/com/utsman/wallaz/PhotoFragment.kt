package com.utsman.wallaz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.utsman.wallaz.data.Photos
import com.utsman.wallaz.di.MainInjector
import kotlinx.android.synthetic.main.photo_fragment.*
import java.lang.Exception

class PhotoFragment : Fragment() {

    private var expanded = false

    private val mainActivity by lazy {
        MainInjector.injectMainActivity(activity as MainActivity)
    }

    private val photo by lazy {
        arguments?.getParcelable("photo") as Photos
    }

    private val sheetBehavior by lazy {
        BottomSheetBehavior.from(bottom_sheet_layout)
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
            else findNavController().popBackStack()
        }

        setupBottomSheetContent()
    }

    @SuppressLint("SetTextI18n")
    private fun setupBottomSheetContent() {
        text_author.text = "Photo by ${photo.user.name}"
        text_location.text = photo.user.location

        text_author.hideIfTextEmpty()
        text_location.hideIfTextEmpty()



    }
}