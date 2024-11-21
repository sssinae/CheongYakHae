package com.example.cheongyakhae.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cheongyakhae.databinding.ActivityFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩 설정
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3초 지연 후 MainActivity로 이동
        lifecycleScope.launch {
            delay(3000) // 3초 지연
            startActivity(Intent(this@FirstActivity, MainActivity::class.java))
            finish() // FirstActivity 종료하여 뒤로 가기 시 다시 돌아오지 않음
        }
    }
}
