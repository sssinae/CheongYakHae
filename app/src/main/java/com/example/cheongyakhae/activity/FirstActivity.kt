package com.example.cheongyakhae.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cheongyakhae.databinding.ActivityFirstBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 설정
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화 작업을 위해 3초 지연 후 MainActivity로 이동
        val splashJob = lifecycleScope.launch {
            delay(3000) // 3초 지연
            navigateToMainActivity() // MainActivity로 이동
        }

        // 다른 이벤트로 인해 Activity가 종료될 경우 코루틴 취소 처리
        lifecycle.addObserver(object : androidx.lifecycle.DefaultLifecycleObserver {
            override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                super.onDestroy(owner)
                splashJob.cancel() // 코루틴 취소
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@FirstActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // FirstActivity 종료하여 뒤로 가기 시 다시 돌아오지 않음
    }
}
