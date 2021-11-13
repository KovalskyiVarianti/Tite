package com.example.tite.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tite.R
import com.example.tite.databinding.ActivityMainBinding
import com.example.tite.databinding.DrawerHeaderBinding
import com.example.tite.presentation.chatlist.ChatListFragmentDirections
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.app.ActivityCompat.startActivityForResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tite.domain.UserManager
import com.example.tite.presentation.extensions.loadAvatar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var drawerHeaderBinding: DrawerHeaderBinding? = null
    private var navController: NavController? = null
    var toolbar: Toolbar? = null
        private set
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setContentView(binding?.root)
        initToolbar()
        initNavController()
        initNavViewButtons()
        val reg = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.uploadAvatar(uri) { avatarUri ->
                drawerHeaderBinding?.avatarImage?.loadAvatar(avatarUri.toString())
            }
        }
        drawerHeaderBinding?.avatarImage?.setOnClickListener {
            reg.launch("image/*")
        }
        lifecycleScope.launchWhenCreated {
            viewModel.selfPersonState.collect { selfPerson ->
                drawerHeaderBinding?.emailText?.text = selfPerson.email
                drawerHeaderBinding?.nameText?.text = selfPerson.name
                drawerHeaderBinding?.avatarImage?.loadAvatar(selfPerson.photo.orEmpty())
            }
        }
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
                    viewModel.signOut()
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
                R.id.contactListFragment -> {
                    navController?.navigate(ChatListFragmentDirections.actionChatListFragmentToContactListFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}