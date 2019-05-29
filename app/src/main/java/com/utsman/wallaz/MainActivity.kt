package com.utsman.wallaz

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawer: Drawer

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

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

        drawer = DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(item1, item2, item3)
                .build()

        val view = LayoutInflater.from(this).inflate(R.layout.header_drawer, null)
        drawer.setHeader(view, false, true, DimenHolder.fromDp(120))
        drawer.setSelection(1, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
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

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.main_host_fragment_app).navigateUp()
    }

    override fun onUserLeaveHint() {
        finish()
    }
}