package com.example.cheongyakhae.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cheongyakhae.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cheongyakhae.R

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화 순서 변경: toggle 초기화 먼저 수행
        initializeToolbar()
        initializeDrawerToggle() // toggle 먼저 초기화
        configureNavigationController()
        setNavigationMenuClickListener()
        setToolbarLogoNavigation()
        setHeaderNavigationListeners()

        // 뒤로가기 버튼 동작 처리
        handleBackPressed()
    }

    private fun initializeToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initializeDrawerToggle() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 드로어가 닫힐 때 선택된 항목 해제
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: android.view.View) {
                clearDrawerSelection()
            }
        })
    }

    private fun configureNavigationController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment,
                R.id.announceFragment,
                R.id.communityFragment,
                R.id.mypageFragment,
                R.id.signupFragment,
                R.id.loginFragment,
                R.id.informationFragment,
                R.id.writeFragment,
                R.id.updateFragment
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // DetailFragment에서는 드로어를 잠그고, 다른 화면에서는 잠금 해제
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
    }

    private fun setNavigationMenuClickListener() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val destinationId = when (menuItem.itemId) {
                R.id.nav_info -> R.id.informationFragment
                R.id.nav_ads -> R.id.announceFragment
                R.id.nav_community -> R.id.communityFragment
                R.id.nav_mypage -> R.id.mypageFragment
                else -> R.id.mainFragment
            }
            if (navController.currentDestination?.id != destinationId) {
                navController.navigate(destinationId)
                menuItem.isChecked = true
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setToolbarLogoNavigation() {
        val toolbarTitle = binding.toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.setOnClickListener {
            navController.navigate(R.id.mainFragment)
        }
    }

    private fun setHeaderNavigationListeners() {
        val headerView = binding.navigationView.getHeaderView(0)
        val loginTextView = headerView.findViewById<TextView>(R.id.login)
        val signupTextView = headerView.findViewById<TextView>(R.id.signup)

        loginTextView.setOnClickListener {
            navController.navigate(R.id.loginFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        signupTextView.setOnClickListener {
            navController.navigate(R.id.signupFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun clearDrawerSelection() {
        val menu = binding.navigationView.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
    }

    private fun handleBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.detailFragment) {
                    navController.popBackStack()
                } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false // 기본 뒤로가기 동작 실행
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
