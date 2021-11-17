package com.example.tite.presentation

import android.content.Intent
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
import com.example.tite.data.network.RetrofitNotificationApi.Companion.CHAT
import com.example.tite.data.network.RetrofitNotificationApi.Companion.ID
import com.example.tite.databinding.ActivityMainBinding
import com.example.tite.databinding.DrawerHeaderBinding
import com.example.tite.presentation.chatlist.ChatListFragmentDirections
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.tite.presentation.extensions.loadAvatar


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
        checkIfOpenedByNotification()
        val reg = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.uploadAvatar(uri) { avatarUri ->
                viewModel.savePhoto(avatarUri)
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
                drawerHeaderBinding?.avatarImage?.loadAvatar(selfPerson.photo)
            }
        }
    }

    private fun checkIfOpenedByNotification() {
        val personId = intent.getStringExtra(ID)
        val chatId = intent.getStringExtra(CHAT)
        if (!personId.isNullOrBlank() && !chatId.isNullOrBlank()) {
            navController?.navigate(
                ChatListFragmentDirections.actionChatListFragmentToMessageListFragment(
                    chatId,
                    personId
                )
            )
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
            it.addOnDestinationChangedListener { controller, destination, _ ->
                binding?.drawer?.setDrawerLockMode(
                    if (destination.id == controller.graph.startDestination) {
                        DrawerLayout.LOCK_MODE_UNLOCKED
                    } else {
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                    }
                )
            }
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