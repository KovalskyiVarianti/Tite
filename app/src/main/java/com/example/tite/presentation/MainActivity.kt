package com.example.tite.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tite.R
import com.example.tite.databinding.ActivityMainBinding
import com.example.tite.databinding.DrawerHeaderBinding
import com.example.tite.domain.UserManager
import com.example.tite.presentation.chatlist.ChatListFragmentDirections
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var drawerHeaderBinding: DrawerHeaderBinding? = null
    private var navController: NavController? = null
    private val userManager: UserManager by inject()
    var toolbar: Toolbar? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setContentView(binding?.root)
        initToolbar()
        initNavController()
        initNavViewButtons()
        drawerHeaderBinding?.emailText?.text = userManager.userEmail
        drawerHeaderBinding?.nameText?.text = userManager.name
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
        navController?.addOnDestinationChangedListener { controller, destination, _ ->
            binding?.drawer?.setDrawerLockMode(
                if (destination.id == controller.graph.startDestination) {
                    DrawerLayout.LOCK_MODE_UNLOCKED
                } else {
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                }
            )
        }
    }

    private fun initNavViewButtons() {
        binding?.navView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.signOutButton -> {
                    userManager.signOut()
                    startActivity(
                        Intent(this, AuthActivity::class.java)
                    )
                    true
                }
                R.id.personListFragment -> {
//                    if (navController?.graph?.startDestination == navController?.currentDestination?.id)
                    navController?.navigate(ChatListFragmentDirections.actionChatListFragmentToPersonListFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}