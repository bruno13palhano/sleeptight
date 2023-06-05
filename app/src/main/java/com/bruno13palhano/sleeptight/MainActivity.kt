package com.bruno13palhano.sleeptight

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var root: LinearLayout

    private val listener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        root.getWindowVisibleDisplayFrame(rect)
        val screenHeight = root.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > screenHeight * 0.15) {
            hideBottomNavigation()
        } else {
            showBottomNavigation()
        }
    }

    override fun onResume() {
        super.onResume()
        root.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun onPause() {
        super.onPause()
        root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        root = findViewById(R.id.main_layout)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavView = findViewById(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return menuItem.onNavDestinationSelected(navController)
            }
        })
    }

    fun showBottomNavigation() {
        bottomNavView.visibility = VISIBLE
    }

    fun hideBottomNavigation() {
        bottomNavView.visibility = GONE
    }
}