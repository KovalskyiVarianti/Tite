package com.example.tite.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tite.R
import com.example.tite.databinding.ActivityMainBinding
import com.example.tite.databinding.DrawerHeaderBinding
import com.example.tite.utils.AuthUtils
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var drawerHeaderBinding: DrawerHeaderBinding? = null
    private var navController: NavController? = null
    private val authUtils : AuthUtils by inject()
    var toolbar: Toolbar? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setContentView(binding?.root)
        initToolbar()
        initNavController()
        drawerHeaderBinding?.emailText?.text = authUtils.userEmail
        drawerHeaderBinding?.nameText?.text = authUtils.userUID
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp(binding?.drawer) == true
    }

    private fun initBindings() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerHeaderBinding =
            binding?.navView?.getHeaderView(0)?.let { DrawerHeaderBinding.bind(it) }
    }

    private fun initToolbar() {
        toolbar = binding?.toolbar
        setSupportActionBar(toolbar)
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let {
            setupActionBarWithNavController(it, binding?.drawer)
            binding?.navView?.setupWithNavController(it)
        }
    }
}