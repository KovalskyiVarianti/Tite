package com.example.tite.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.tite.R

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    var toolbar: Toolbar? = null
    private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() == true
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(this, it)
        }
    }
}