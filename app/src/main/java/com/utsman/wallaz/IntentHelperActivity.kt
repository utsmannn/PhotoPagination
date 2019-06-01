package com.utsman.wallaz

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class IntentHelperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id")
        if (id != null) {

            val file = File(Environment.getExternalStorageDirectory(), "/.Wallaz/$id.jpg")
            setupWallpaper(this, file)
            //finish()
        }

    }
}