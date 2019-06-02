package com.utsman.wallaz

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST = 1
    private lateinit var drawer: Drawer

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        drawer = DrawerBuilder()
            .withActivity(this)
            .addDrawerItems(item1, item2, item3, DividerDrawerItem(), item4)
            .build()

        val view = LayoutInflater.from(this).inflate(R.layout.header_drawer, null)
        drawer.setHeader(view, false, true, DimenHolder.fromDp(120))

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

    /*override fun onUserLeaveHint() {
        finish()
    }*/
}