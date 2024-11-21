package com.example.cheongyakhae

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 네비게이션 설정
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        // AppBarConfiguration 설정
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment, R.id.announceFragment, R.id.communityFragment, R.id.mypageFragment, R.id.signupFragment, R.id.loginFragment, R.id.informationFragment, R.id.writeFragment, R.id.updateFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // 햄버거 버튼 설정
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 로고 클릭 시 메인 프래그먼트로 이동
        val toolbarTitle = binding.toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.setOnClickListener {
            navController.navigate(R.id.mainFragment)
            setupActionBarWithNavController(navController, appBarConfiguration)
        }

        // 네비게이션 메뉴 클릭 처리
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val destinationId = when (menuItem.itemId) {
                R.id.nav_info -> R.id.informationFragment
                R.id.nav_ads -> R.id.announceFragment
                R.id.nav_community -> R.id.communityFragment
                R.id.nav_mypage -> R.id.mypageFragment
                else -> R.id.mainFragment
            }
            navController.navigate(destinationId)
            clearDrawerSelection()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }



        // 네비게이션 헤더 클릭 이벤트
        val headerView = binding.navigationView.getHeaderView(0)
        val loginTextView = headerView.findViewById<TextView>(R.id.login)
        val signupTextView = headerView.findViewById<TextView>(R.id.signup)

        // 로그인 클릭 리스너
        loginTextView.setOnClickListener {
            navController.navigate(R.id.loginFragment) // Navigation Graph에 정의된 ID 사용
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 회원가입 클릭 리스너
        signupTextView.setOnClickListener {
            navController.navigate(R.id.signupFragment) // Navigation Graph에 정의된 ID 사용
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }


    }
    private fun clearDrawerSelection() {
        val menu = binding.navigationView.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
