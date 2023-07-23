package com.swapnil.myapplication.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import com.swapnil.myapplication.R
import com.swapnil.myapplication.utils.NetworkUtils

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var llInternetNotAvailable: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        llInternetNotAvailable = findViewById(R.id.internet_not_available)
        setSupportActionBar(toolbar)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)

        NetworkUtils.init(this)
        NetworkUtils.isConnected.observe(this) { isConnected ->
            Log.d(TAG, "onCreate: isConnected? $isConnected")
            if (isConnected) {
                llInternetNotAvailable.visibility = View.GONE
            } else {
                llInternetNotAvailable.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navigateUp(navHostFragment.navController, null)
                || super.onSupportNavigateUp())
    }
}