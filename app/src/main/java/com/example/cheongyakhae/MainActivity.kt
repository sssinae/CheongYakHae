package com.example.cheongyakhae

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Drawer 설정
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 네비게이션 헤더에 클릭 이벤트 연결
        val headerView = binding.navigationView.getHeaderView(0) // 네비게이션 헤더 가져오기
        val loginTextView = headerView.findViewById<TextView>(R.id.login)
        val signupTextView = headerView.findViewById<TextView>(R.id.signup)

        // 로그인 클릭 리스너
        loginTextView.setOnClickListener {
            replaceFragment(LoginFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 회원가입 클릭 리스너
        signupTextView.setOnClickListener {
            replaceFragment(SignupFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }



        // Drawer 메뉴 클릭 리스너
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_community -> CommunityFragment()
                R.id.nav_ads -> AnnounceFragment()
                R.id.nav_mypage -> MypageFragment()
                else -> CommunityFragment()
            }

            // 선택된 Fragment 표시
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // 초기 화면
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CommunityFragment())
            .commit()
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}
