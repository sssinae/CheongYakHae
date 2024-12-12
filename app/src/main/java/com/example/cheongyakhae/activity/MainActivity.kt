package com.example.cheongyakhae.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cheongyakhae.R
import com.example.cheongyakhae.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userTextView: TextView
    private lateinit var loginTextView: TextView
    private lateinit var signupTextView: TextView
    private lateinit var logoutTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance()

        // viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 네비게이션 설정
        setupNavigation()

        // 네비게이션 헤더 초기화
        initializeNavigationHeader()

        // UI 업데이트 (사용자 정보)
        updateNavigationHeader()
    }

    private fun setupNavigation() {
        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // AppBarConfiguration 설정 (뒤로가기 아이콘 대신 햄버거 아이콘만 표시)
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
                R.id.detailFragment
            ),
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

        // 로고 클릭 시 메인 프래그먼트로 이동
        val toolbarTitle = binding.toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.setOnClickListener {
            navController.navigate(R.id.mainFragment)
        }
    }

    private fun initializeNavigationHeader() {
        val headerView = binding.navigationView.getHeaderView(0)

        // 네비게이션 헤더 UI 요소 가져오기
        userTextView = headerView.findViewById(R.id.user_name)
        loginTextView = headerView.findViewById(R.id.login)
        signupTextView = headerView.findViewById(R.id.signup)
        logoutTextView = headerView.findViewById(R.id.logout)

        // 로그인 클릭 리스너
        loginTextView.setOnClickListener {
            navController.navigate(R.id.loginFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 회원가입 클릭 리스너
        signupTextView.setOnClickListener {
            navController.navigate(R.id.signupFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 로그아웃 클릭 리스너
        logoutTextView.setOnClickListener {
            auth.signOut() // Firebase 로그아웃
            updateNavigationHeader() // 헤더 업데이트
            navController.navigate(R.id.mainFragment) // 로그아웃 후 메인 화면으로 이동
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    fun updateNavigationHeader() {
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            // 로그인된 경우
            userTextView.text = currentUser.email
            loginTextView.visibility = View.GONE
            signupTextView.visibility = View.GONE
            logoutTextView.visibility = View.VISIBLE
        } else {
            // 로그아웃된 경우
            userTextView.text = "사용자 님"
            loginTextView.visibility = View.VISIBLE
            signupTextView.visibility = View.VISIBLE
            logoutTextView.visibility = View.GONE
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
        // 뒤로가기 아이콘 대신 항상 햄버거 아이콘을 사용
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
