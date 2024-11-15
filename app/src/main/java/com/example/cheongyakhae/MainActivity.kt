package com.example.cheongyakhae

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        // View Binding 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)


        // 기본 액션바 타이틀 숨기기
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 드로어 토글 설정 (햄버거 아이콘과 DrawerLayout 연결)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 드로어 메뉴 아이템 클릭 리스너 설정
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // 메뉴 클릭 시 해당 Fragment로 전환
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_info -> InformationFragment()
                R.id.nav_ads -> AnnounceFragment()
                R.id.nav_community -> CommunityFragment()
                R.id.nav_mypage -> MypageFragment()
                else -> InformationFragment() // 기본 Fragment
            }

            // 선택된 Fragment를 fragment_container에 로드
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 뒤로 가기 기능을 위해 백스택에 추가
                .commit()

            // 드로어 닫기
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 햄버거 아이콘 클릭 시 드로어 열기
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // 서치뷰
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}
